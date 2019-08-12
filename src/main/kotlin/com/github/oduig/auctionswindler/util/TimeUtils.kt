package com.github.oduig.auctionswindler.util

import org.springframework.stereotype.Service

@Service
class TimeUtils {

  fun now(): Long = System.currentTimeMillis()
}
