package com.github.oduig.auctionswindler.api

import io.mockk.*
import org.junit.Assert
import org.junit.Test

class AuctionSwindlerControllerTest {

  private var starterServiceMock = mockk<AuctionSwindlerService>()

  private val starterController = AuctionSwindlerController(starterServiceMock)

  @Test
  fun homeTest() {
    // given

    // when
    val reply = starterController.home()

    // then
    Assert.assertEquals("Auction House Swindler is online!", reply.message)
  }

  @Test
  fun slothTest() {
    // given
    givenAuctionSwindlerServiceWaits()

    // when
    val reply = starterController.sloth()

    // then
    Assert.assertEquals("...", reply.message)
    thenAuctionSwindlerServiceWaited()
  }

  private fun givenAuctionSwindlerServiceWaits() {
    every { starterServiceMock.waitForABit() } just Runs
  }

  private fun thenAuctionSwindlerServiceWaited() {
    verify { starterServiceMock.waitForABit() }
  }
}
