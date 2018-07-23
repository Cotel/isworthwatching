package com.cotel.isworthwatching.base

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun <A> listResponder(): Responder<List<A>> = object : Responder<List<A>> {
  override fun List<A>.respond(): ResponseEntity<List<A>> =
      ResponseEntity.ok(this)
}

fun <A> creationSuccessResponder(): Responder<A> = object : Responder<A> {
  override fun A.respond(): ResponseEntity<A> =
      ResponseEntity(this, HttpStatus.CREATED)
}

fun errorResponder(httpStatus: HttpStatus): Responder<Map<String, String>> = object : Responder<Map<String, String>> {
  override fun Map<String, String>.respond(): ResponseEntity<Map<String, String>> =
      ResponseEntity(this, httpStatus)
}

fun notFoundResponder(): Responder<Map<String, String>> =
    errorResponder(HttpStatus.NOT_FOUND)

fun <A> defaultNotFoundResponder(kclass: Class<A>, id: String) =
    notFoundResponder().run {
      val map = mapOf("error" to "No ${kclass.simpleName} found with $id")
      map.respond()
    }

fun <A> entityResponder(): Responder<A> = object : Responder<A> {
  override fun A.respond(): ResponseEntity<A> =
      ResponseEntity.ok(this)
}
