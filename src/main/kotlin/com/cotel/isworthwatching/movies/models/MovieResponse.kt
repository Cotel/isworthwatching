package com.cotel.isworthwatching.movies.models

import com.cotel.isworthwatching.base.EntityMapper
import java.time.Instant
import java.util.*

data class MovieResponse(
    val id: String = "",
    val name: String = "",
    val releaseDate: Long = 0L,
    val positiveVotes: Int = 0,
    val negativeVotes: Int = 0
) {
  companion object {
    val domainMapper = object : EntityMapper<MovieResponse, Movie> {
      override fun MovieResponse.get(): Movie = Movie(
          this.id,
          this.name,
          Date.from(Instant.ofEpochMilli(this.releaseDate)),
          this.positiveVotes,
          this.negativeVotes
      )

      override fun Movie.reverseGet(): MovieResponse = MovieResponse(
          this.id,
          this.name,
          this.releaseDate.time,
          this.positiveVotes,
          this.negativeVotes
      )
    }
  }
}