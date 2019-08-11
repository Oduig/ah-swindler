package com.github.oduig.auctionswindler.api

import com.github.oduig.auctionswindler.config.properties.AuctionSwindlerConfigProperties
import com.github.oduig.auctionswindler.util.SoundSleeper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class AuctionSwindlerServiceTest {

  private val starterConfigProperties = AuctionSwindlerConfigProperties(slothDelayMs = 100)

  private var soundSleeperMock = mockk<SoundSleeper>()

  private val starterService = AuctionSwindlerService(starterConfigProperties, soundSleeperMock)

  @Test
  fun starterServiceTest() {
    // given
    givenSoundSleeperSleepsFor(100)

    // when
    starterService.waitForABit()

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
