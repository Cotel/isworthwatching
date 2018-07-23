package com.cotel.isworthwatching.movies.models

import com.cotel.isworthwatching.base.EntityMapper
import com.cotel.isworthwatching.base.toSnakeCase
import java.time.Instant
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Movies")
data class MovieEntity(
    @Id val id: String = "",
    val name: String = "",
    val searchName: String = "",
    val releaseDate: Long = 0L,
    val positiveVotes: Int = 0,
    val negativeVotes: Int = 0
) {
  companion object {
    val domainMapper = object : EntityMapper<MovieEntity, Movie> {
      override fun MovieEntity.get(): Movie = Movie(
          this.id,
          this.name,
          Date.from(Instant.ofEpochMilli(this.releaseDate)),
          this.positiveVotes,
          this.negativeVotes
      )

      override fun Movie.reverseGet(): MovieEntity = MovieEntity(
          this.id,
          this.name,
          this.name.toUpperCase().toSnakeCase(),
          this.releaseDate.time,
          this.positiveVotes,
          this.negativeVotes
      )
    }

    val addRequestMapper = object : EntityMapper<MovieEntity, AddMovieRequest> {
      override fun MovieEntity.get(): AddMovieRequest = AddMovieRequest(
          this.name,
          this.releaseDate
      )

      override fun AddMovieRequest.reverseGet(): MovieEntity = MovieEntity(
          id = UUID.randomUUID().toString(),
          name = this.name,
          searchName = this.name.toUpperCase().toSnakeCase(),
          releaseDate = this.releaseDate
      )
    }
  }
}