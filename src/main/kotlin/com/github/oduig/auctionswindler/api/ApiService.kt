package com.github.oduig.auctionswindler.api

import com.github.oduig.auctionswindler.blizzard.BlizzardApi
import com.github.oduig.auctionswindler.blizzard.BlizzardRegion
import com.github.oduig.auctionswindler.blizzard.generated.WowAuctionPage
import com.github.oduig.auctionswindler.blizzard.generated.WowCharacter
import com.github.oduig.auctionswindler.config.properties.AuctionSwindlerConfigProperties
import com.github.oduig.auctionswindler.util.SoundSleeper
import nl.komponents.kovenant.Promise
import org.springframework.stereotype.Service

@Service
class ApiService(private val auctionSwindlerConfigProperties: AuctionSwindlerConfigProperties,
                 private val soundSleeper: SoundSleeper,
                 private val blizzardApi: BlizzardApi) {

  fun waitForABit() {
    soundSleeper.sleep(auctionSwindlerConfigProperties.slothDelayMs!!)
  }

  fun character(region: BlizzardRegion, server: String, character: String): Promise<WowCharacter, Exception> {
    return blizzardApi.characterProfile(region, server, character)
  }

  fun auctions(region: BlizzardRegion, server: String, pageNumber: Int): Promise<WowAuctionPage, Exception> {
    return blizzardApi.auctionPage(region, server, pageNumber)
  }
}
