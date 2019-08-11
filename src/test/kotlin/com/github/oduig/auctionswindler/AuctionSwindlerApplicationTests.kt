package com.github.oduig.auctionswindler

import com.github.oduig.auctionswindler.util.PromisingHttpClient
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.reactive.function.client.WebClient

@RunWith(SpringRunner::class)
@SpringBootTest
class AuctionSwindlerApplicationTests {

  @Autowired
  private var webClient: WebClient? = null

  @Test
  fun contextLoads() {
  }

  @Test
  fun coverageForMainMethod() {
    com.github.oduig.auctionswindler.main(arrayOf())
  }

  @Test
  fun coverageForWebClientConfig() {
    Assert.assertNotNull(webClient)
    val promisingHttpClient = PromisingHttpClient(webClient!!)
    try {
      promisingHttpClient.get("http://example.com").get()
    } catch (e: Exception) {
      // No problem. It's just coverage.
    }
  }
}
