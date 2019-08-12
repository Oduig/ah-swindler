package com.github.oduig.auctionswindler.api

import com.github.oduig.auctionswindler.api.model.SimpleTextMessage
import com.github.oduig.auctionswindler.blizzard.BlizzardRegion
import com.github.oduig.auctionswindler.blizzard.generated.WowAuctionPage
import com.github.oduig.auctionswindler.blizzard.generated.WowCharacter
import com.github.oduig.auctionswindler.util.mono
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.zalando.problem.Problem
import org.zalando.problem.Status
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/")
class ApiController(private val apiService: ApiService) {

  @GetMapping("")
  fun home(): SimpleTextMessage {
    return SimpleTextMessage("Auction House Swindler is online!")
  }

  @GetMapping("/api/v1/sloth")
  // Enable web security for request authorization. Some business code should be written for this to work.
  // @PreAuthorize("hasPermission('TIMEKEEPING', 'READ')")
  fun sloth(): SimpleTextMessage {
    apiService.waitForABit()
    return SimpleTextMessage("...")
  }

  @GetMapping("/api/v1/region/{region}/server/{server}/character/{character}")
  fun character(@PathVariable region: String, @PathVariable server: String, @PathVariable character: String): Mono<WowCharacter> {
    return withRegion(region) {
      apiService.character(it, server, character).mono()
    }
  }

  @GetMapping("/api/v1/region/{region}/server/{server}/auctions/page/{pageNumber}")
  fun auctions(@PathVariable region: String, @PathVariable server: String, @PathVariable pageNumber: Int): Mono<WowAuctionPage> {
    return withRegion(region) {
      apiService.auctions(it, server, pageNumber).mono()
    }
  }

  private fun <T> withRegion(region: String, body: (BlizzardRegion) -> Mono<T>): Mono<T> {
    val blizzardRegions = BlizzardRegion.values().associateBy(BlizzardRegion::slug)
    return blizzardRegions[region]?.let {
      body(it)
    } ?: throw Problem.valueOf(Status.BAD_REQUEST)
  }
}
