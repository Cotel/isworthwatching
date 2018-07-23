package com.cotel.isworthwatching.base

interface EntityMapper<A, B> {

  fun A.get(): B

  fun B.reverseGet(): A

}