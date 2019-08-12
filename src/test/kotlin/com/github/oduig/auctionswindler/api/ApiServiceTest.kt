package com.github.oduig.auctionswindler.api

import com.github.oduig.auctionswindler.blizzard.BlizzardApi
import com.github.oduig.auctionswindler.config.properties.AuctionSwindlerConfigProperties
import com.github.oduig.auctionswindler.util.SoundSleeper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class ApiServiceTest {

  private val auctionSwindlerConfigProperties = AuctionSwindlerConfigProperties(slothDelayMs = 100)

  private var soundSleeperMock = mockk<SoundSleeper>()
  private var blizzardApiMock = mockk<BlizzardApi>()

  private val apiService = ApiService(auctionSwindlerConfigProperties, soundSleeperMock, blizzardApiMock)

  @Test
  fun apiServiceTest() {
    // given
    givenSoundSleeperSleepsFor(100)

    // when
    apiService.waitForABit()

    // then
    thenSoundSleeperSlept()
  }

  private fun givenSoundSleeperSleepsFor(expectedSleepDuration: Long) {
    every { soundSleeperMock.sleep(expectedSleepDuration) } returns expectedSleepDuration
  }

  private fun thenSoundSleeperSlept() {
    verify { soundSleeperMock.sleep(any()) }
  }
}
