package com.github.oduig.auctionswindler.util

import java.util.Base64

object Base64 {
  fun encode(s: String): String = Base64.getEncoder().encode(s.toByteArray(Charsets.UTF_8)).toString(Charsets.UTF_8)
  fun decode(s: String): String = Base64.getDecoder().decode(s.toByteArray(Charsets.UTF_8)).toString(Charsets.UTF_8)
}
