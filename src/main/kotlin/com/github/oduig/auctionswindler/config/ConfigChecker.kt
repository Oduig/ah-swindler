package com.github.oduig.auctionswindler.config

import com.github.oduig.auctionswindler.config.properties.AuctionSwindlerConfigProperties
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ConfigChecker(private val auctionSwindlerConfigProperties: AuctionSwindlerConfigProperties) {

  @EventListener(ApplicationReadyEvent::class)
  fun checkConfig() {
    // Perform a basic check to make sure the configuration is loaded on startup
    requireNotNull(auctionSwindlerConfigProperties.cors)
  }
}
