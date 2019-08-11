package com.github.oduig.auctionswindler

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration


@SpringBootApplication(exclude = [ErrorMvcAutoConfiguration::class, SecurityAutoConfiguration::class])
class AuctionSwindlerApplication

fun main(args: Array<String>) {
  SpringApplication.run(AuctionSwindlerApplication::class.java, *args)
}
