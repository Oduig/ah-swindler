package com.github.oduig.auctionswindler.util

import mu.KLogging
import nl.komponents.kovenant.Kovenant
import nl.komponents.kovenant.Promise
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Mono
import java.net.URI

typealias WebClientUri = (UriBuilder) -> URI

@Component
class PromisingHttpClient(private val springWebClient: WebClient) {
  companion object: KLogging()

  fun get(uri: String,
          headers: Map<String, String> = emptyMap()): Promise<String, Exception> {
    val request = springWebClient.get()
        .uri(uri)
        .accept(MediaType.APPLICATION_JSON_UTF8)
    headers.forEach {
      request.header(it.key, it.value)
    }

    return send(request)
  }

  fun get(uri: WebClientUri,
          headers: Map<String, String> = emptyMap()): Promise<String, Exception> {
    val request = springWebClient.get()
        .uri(uri)
        .accept(MediaType.APPLICATION_JSON_UTF8)
    headers.forEach {
      request.header(it.key, it.value)
    }

    return send(request)
  }

  fun post(uri: String,
           jsonBody: String,
           headers: Map<String, String> = emptyMap()): Promise<String, Exception> {
    val request = springWebClient.post()
        .uri(uri)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .body(BodyInserters.fromObject(jsonBody))
        .accept(MediaType.ALL)
    headers.forEach {
      request.header(it.key, it.value)
    }

    return send(request)
  }

  fun post(uri: WebClientUri,
           jsonBody: String,
           headers: Map<String, String> = emptyMap()): Promise<String, Exception> {
    val request = springWebClient.post()
        .uri(uri)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .body(BodyInserters.fromObject(jsonBody))
        .accept(MediaType.ALL)
    headers.forEach {
      request.header(it.key, it.value)
    }

    return send(request)
  }

  fun post(uri: String,
           formParams: Map<String, List<String>>,
           headers: Map<String, String> = emptyMap()): Promise<String, Exception> {
    val multiValueFormParams = LinkedMultiValueMap<String, String>(formParams)
    val request = springWebClient.post()
        .uri(uri)
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .body(BodyInserters.fromFormData(multiValueFormParams))
        .accept(MediaType.ALL)
    headers.forEach {
      request.header(it.key, it.value)
    }

    return send(request)
  }

  fun post(uri: WebClientUri,
           formParams: Map<String, List<String>>,
           headers: Map<String, String> = emptyMap()): Promise<String, Exception> {
    val multiValueFormParams = LinkedMultiValueMap<String, String>(formParams)
    val request = springWebClient.post()
        .uri(uri)
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .body(BodyInserters.fromFormData(multiValueFormParams))
        .accept(MediaType.ALL)
    headers.forEach {
      request.header(it.key, it.value)
    }

    return send(request)
  }

  fun uri(scheme: String, hostName: String, path: String,
          queryParams: Map<String, List<String>> = emptyMap()): (UriBuilder) -> URI = {
    it.scheme(scheme).host(hostName).path(path)
        .queryParams(LinkedMultiValueMap(queryParams))
        .build()
  }

  private fun send(request: WebClient.RequestHeadersSpec<*>): Promise<String, Exception> {
    val deferred = Kovenant.deferred<String, Exception>()
    val futureResponse: Mono<String> = try {
      request.retrieve().bodyToMono(String::class.java)
    } catch (t: Exception) {
      Mono.error<String>(t)
    }
    val handleSuccess = deferred::resolve
    val handleFailure: (Throwable) -> Unit = {
      val exception = when (it) {
        is Exception -> it
        else -> RuntimeException("Throwable was wrapped into an exception.", it)
      }
      deferred.reject(exception)
    }
    // It says this next line is not test-covered, but it is.
    futureResponse.subscribe(handleSuccess, handleFailure)
    return deferred.promise
  }
}
