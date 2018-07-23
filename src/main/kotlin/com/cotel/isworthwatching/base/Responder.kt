package com.cotel.isworthwatching.base

import org.springframework.http.ResponseEntity

interface Responder<A> {
  fun A.respond(): ResponseEntity<A>
}