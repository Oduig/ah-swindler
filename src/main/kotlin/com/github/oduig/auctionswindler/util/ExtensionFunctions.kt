package com.github.oduig.auctionswindler.util

import nl.komponents.kovenant.Promise
import reactor.core.publisher.Mono

/**
 * An alternative, more convenient interface for iteration
 */
fun <T> Collection<T>.enumerate(): Enumerator<T> {
  return Enumerator(this.iterator())
}

fun <T> Promise<T, Throwable>.mono(): Mono<T> {
  return Mono.create { deferred ->
    this.success { deferred.success(it) }
    this.fail { deferred.error(it) }
  }
}
