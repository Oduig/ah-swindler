package com.github.oduig.auctionswindler.api

import com.github.oduig.auctionswindler.config.properties.AuctionSwindlerConfigProperties
import com.github.oduig.auctionswindler.util.SoundSleeper
import org.springframework.stereotype.Service

@Service
class AuctionSwindlerService(private val starterConfigProperties: AuctionSwindlerConfigProperties,
                             private val soundSleeper: SoundSleeper) {

  fun waitForABit() {
    soundSleeper.sleep(starterConfigProperties.slothDelayMs!!)
  }
}
