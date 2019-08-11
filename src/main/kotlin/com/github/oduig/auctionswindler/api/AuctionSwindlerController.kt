package com.github.oduig.auctionswindler.api

import com.github.oduig.auctionswindler.api.model.SimpleTextMessage
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class AuctionSwindlerController(private val auctionSwindlerService: AuctionSwindlerService) {

  @GetMapping("")
  fun home(): SimpleTextMessage {
    return SimpleTextMessage("Auction House Swindler is online!")
  }

  @GetMapping("/api/v1/sloth")
  // Enable web security for request authorization. Some business code should be written for this to work.
  // @PreAuthorize("hasPermission('TIMEKEEPING', 'READ')")
  fun sloth(): SimpleTextMessage {
    auctionSwindlerService.waitForABit()
    return SimpleTextMessage("...")
  }
}
