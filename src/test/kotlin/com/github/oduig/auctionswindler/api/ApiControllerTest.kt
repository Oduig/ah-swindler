package com.github.oduig.auctionswindler.api

import io.mockk.*
import org.junit.Assert
import org.junit.Test

class ApiControllerTest {

  private var apiServiceMock = mockk<ApiService>()

  private val apiController = ApiController(apiServiceMock)

  @Test
  fun homeTest() {
    // given

    // when
    val reply = apiController.home()

    // then
    Assert.assertEquals("Auction House Swindler is online!", reply.message)
  }

  @Test
  fun slothTest() {
    // given
    givenAuctionSwindlerServiceWaits()

    // when
    val reply = apiController.sloth()

    // then
    Assert.assertEquals("...", reply.message)
    thenAuctionSwindlerServiceWaited()
  }

  private fun givenAuctionSwindlerServiceWaits() {
    every { apiServiceMock.waitForABit() } just Runs
  }

  private fun thenAuctionSwindlerServiceWaited() {
    verify { apiServiceMock.waitForABit() }
  }
}
