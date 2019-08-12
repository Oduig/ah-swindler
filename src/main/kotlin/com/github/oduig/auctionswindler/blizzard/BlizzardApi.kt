package com.github.oduig.auctionswindler.blizzard

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.oduig.auctionswindler.blizzard.generated.WowAuctionList
import com.github.oduig.auctionswindler.blizzard.generated.WowAuctionPage
import com.github.oduig.auctionswindler.blizzard.generated.WowAuctions
import com.github.oduig.auctionswindler.blizzard.generated.WowCharacter
import com.github.oduig.auctionswindler.util.PromisingHttpClient
import mu.KLogging
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.functional.bind
import nl.komponents.kovenant.then
import org.springframework.stereotype.Component
import java.lang.RuntimeException
import java.text.MessageFormat

@Component
class BlizzardApi(private val blizzardAuthenticator: BlizzardAuthenticator,
                  private val httpClient: PromisingHttpClient,
                  private val json: ObjectMapper) {

  companion object: KLogging() {
    const val BLIZZ_API_SCHEME = "https"
    const val BLIZZ_API_HOSTNAME = "{0}.api.blizzard.com"
    const val BLIZZ_API_NAMESPACE = "profile-{0}"
    const val BLIZZ_API_LOCALE = "en_US"
    const val BLIZZ_API_PATH_CHARACTER = "/profile/wow/character/{0}/{1}"
    const val BLIZZ_API_PATH_AUCTIONS = "/wow/auction/data/{0}"
  }

  fun characterProfile(region: BlizzardRegion, wowServer: String, wowCharacter: String):
      Promise<WowCharacter, Exception> {
    val queryParams = mapOf(
        Pair("locale", listOf(BLIZZ_API_LOCALE)),
        Pair("namespace", listOf(MessageFormat.format(BLIZZ_API_NAMESPACE, region.slug)))
    )
    val headers = mapOf(Pair("Authorization", "Bearer ${blizzardAuthenticator.token()}"))
    val hostName = MessageFormat.format(BLIZZ_API_HOSTNAME, region.slug)
    val path = MessageFormat.format(BLIZZ_API_PATH_CHARACTER, wowServer, wowCharacter)
    val uri = httpClient.uri(BLIZZ_API_SCHEME, hostName, path, queryParams)
    val response = httpClient.get(uri, headers)
    return response.then {
      json.readValue<WowCharacter>(it)
    }
  }

  fun auctionPage(region: BlizzardRegion, wowServer: String, pageNumber: Int): Promise<WowAuctionPage, Exception> {
    val auctions = auctions(region, wowServer)
    return auctions.bind {
      auctionPage(region, it, pageNumber)
    }
  }

  private fun auctionPage(region: BlizzardRegion, wowAuctions: WowAuctions, pageNumber: Int): Promise<WowAuctionPage, Exception> {
    return if (wowAuctions.files.size <= pageNumber) {
      Promise.ofFail(RuntimeException("End of auction stream."))
    } else {
      fetchAuctionListing(region, wowAuctions.files[pageNumber].url).then {
        WowAuctionPage(pageNumber, wowAuctions.files.size, it)
      }
    }
  }

  private fun auctions(region: BlizzardRegion, wowServer: String): Promise<WowAuctions, Exception> {
    val queryParams = mapOf(
        Pair("locale", listOf(BLIZZ_API_LOCALE)),
        Pair("namespace", listOf(MessageFormat.format(BLIZZ_API_NAMESPACE, region.slug)))
    )
    val headers = mapOf(Pair("Authorization", "Bearer ${blizzardAuthenticator.token()}"))
    val hostName = MessageFormat.format(BLIZZ_API_HOSTNAME, region.slug)
    val path = MessageFormat.format(BLIZZ_API_PATH_AUCTIONS, wowServer)
    val uri = httpClient.uri(BLIZZ_API_SCHEME, hostName, path, queryParams)
    val response = httpClient.get(uri, headers)
    return response.then {
      json.readValue<WowAuctions>(it)
    }
  }

  private fun fetchAuctionListing(region: BlizzardRegion, url: String): Promise<WowAuctionList, Exception> {
    return fetch(region, url).then {
      json.readValue<WowAuctionList>(it)
    }
  }

  private fun fetch(region: BlizzardRegion, url: String): Promise<String, Exception> {
    val headers = mapOf(Pair("Authorization", "Bearer ${blizzardAuthenticator.token()}"))
    val response = httpClient.get(url, headers)
    return response
  }
}
