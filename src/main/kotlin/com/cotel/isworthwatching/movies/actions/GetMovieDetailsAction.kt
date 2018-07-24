package com.cotel.isworthwatching.movies.actions

import arrow.core.Option
import arrow.effects.IO
import com.cotel.isworthwatching.base.EntityMapper
import com.cotel.isworthwatching.base.Responder
import com.cotel.isworthwatching.base.defaultNotFoundResponder
import com.cotel.isworthwatching.base.entityResponder
import com.cotel.isworthwatching.base.notFoundResponder
import com.cotel.isworthwatching.movies.queries.GetMovie
import com.cotel.isworthwatching.movies.queries.GetMovieQuery
import com.cotel.isworthwatching.movies.queries.GetMovieUseCase
import com.cotel.isworthwatching.movies.models.Movie
import com.cotel.isworthwatching.movies.models.MovieEntity
import com.cotel.isworthwatching.movies.MoviesRepository
import com.cotel.isworthwatching.movies.models.MovieResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/movies")
class GetMovieDetailsAction(private val repository: MoviesRepository) :
    EntityMapper<MovieEntity, Movie> by MovieEntity.domainMapper,
    Responder<MovieResponse> by entityResponder() {

  @GetMapping("/{id}")
  fun invoke(@PathVariable("id") id: String): ResponseEntity<*> {
    return object : GetMovieUseCase {
      override val getMovie: GetMovie = {
        IO {
          Option.fromNullable(repository.findOneById(it))
              .map { it.get() }
        }
      }
    }.run {
      val result = GetMovieQuery(id).runUseCase().unsafeRunSync()

      result.fold(
          { defaultNotFoundResponder(Movie::class.java, id) },
          { MovieResponse.domainMapper.run { it.reverseGet().respond() } }
      )

    }
  }

}