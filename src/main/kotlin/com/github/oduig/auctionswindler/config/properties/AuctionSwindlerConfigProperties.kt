package com.github.oduig.auctionswindler.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "auction-swindler")
data class AuctionSwindlerConfigProperties(
    // These need to be nullable so Spring Boot can load them
    var slothDelayMs: Long? = null,
    var cors: CorsConfigProperties? = CorsConfigProperties(),
    var webClient: WebClientConfigProperties? = WebClientConfigProperties()
)
