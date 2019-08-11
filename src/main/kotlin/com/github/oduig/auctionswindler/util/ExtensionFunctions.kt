package com.github.oduig.auctionswindler.util

/**
 * An alternative, more convenient interface for iteration
 */
fun <T> Collection<T>.enumerate(): Enumerator<T> {
  return Enumerator(this.iterator())
}
