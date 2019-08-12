package com.github.oduig.auctionswindler.blizzard

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.oduig.auctionswindler.util.Base64
import com.github.oduig.auctionswindler.util.PromisingHttpClient
import com.github.oduig.auctionswindler.util.TimeUtils
import mu.KLogging
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.then
import org.springframework.stereotype.Component

@Component
class BlizzardAuthenticator(private val httpClient: PromisingHttpClient,
                            private val timeUtils: TimeUtils,
                            private val blizzardCredentials: BlizzardCredentials,
                            private val json: ObjectMapper) {

  companion object: KLogging() {
    const val BLIZZ_API_SCHEME = "https"
    const val BLIZZ_API_HOSTNAME = "eu.battle.net"
    const val BLIZZ_API_TOKEN_PATH = "/oauth/token"
    const val BLIZZ_API_TOKEN_MIN_VALIDITY_MS = 1000L
  }

  private lateinit var accessToken: String
  private var expiresOn: Long = 0

  init {
    waitForNewToken()
  }

  fun token(): String {
    if (expiresOn - timeUtils.now() < BLIZZ_API_TOKEN_MIN_VALIDITY_MS) {
      waitForNewToken()
    }
    return accessToken
  }

  private fun waitForNewToken() {
    val tokenResponse = freshToken().get()
    accessToken = tokenResponse.accessToken
    expiresOn = tokenResponse.expiresIn + timeUtils.now()
    logger.info { "Fetched token '$accessToken' from the Blizzard API."}
  }

  private fun freshToken(): Promise<TokenResponse, Exception> {
    val formParams = mapOf(Pair("grant_type", listOf("client_credentials")))
    val clientId = blizzardCredentials.clientId
    val clientSecret = blizzardCredentials.clientSecret
    val base64Credentials = Base64.encode("$clientId:$clientSecret")
    val headers = mapOf(Pair("Authorization", "Basic $base64Credentials"))
    val uri = httpClient.uri(BLIZZ_API_SCHEME, BLIZZ_API_HOSTNAME, BLIZZ_API_TOKEN_PATH)
    val response = httpClient.post(uri, formParams, headers)

    return response.then {
      json.readValue<TokenResponse>(it)
    }
  }
}
