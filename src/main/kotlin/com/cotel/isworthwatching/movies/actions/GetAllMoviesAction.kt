package com.cotel.isworthwatching.movies.actions

import arrow.effects.IO
import com.cotel.isworthwatching.base.EntityMapper
import com.cotel.isworthwatching.base.Responder
import com.cotel.isworthwatching.base.listResponder
import com.cotel.isworthwatching.movies.queries.GetAllMovies
import com.cotel.isworthwatching.movies.queries.GetAllMoviesQuery
import com.cotel.isworthwatching.movies.queries.GetAllMoviesUseCase
import com.cotel.isworthwatching.movies.models.Movie
import com.cotel.isworthwatching.movies.models.MovieEntity
import com.cotel.isworthwatching.movies.MoviesRepository
import com.cotel.isworthwatching.movies.models.MovieResponse
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/movies")
class GetAllMoviesAction(private val repository: MoviesRepository) :
    EntityMapper<MovieEntity, Movie> by MovieEntity.domainMapper,
    Responder<List<MovieResponse>> by listResponder() {

  @GetMapping
  fun invoke(@RequestParam("page") page: Int,
             @RequestParam("page_size") pageSize: Int): ResponseEntity<List<MovieResponse>> {
    return object : GetAllMoviesUseCase {
      override val query: GetAllMovies = { page, pageSize ->
        IO { repository.findAll(PageRequest.of(page, pageSize)) }
            .map { it.map { it.get() }.toList() }
      }
    }.run {
      val movies = GetAllMoviesQuery(page, pageSize).runUseCase().unsafeRunSync()

      MovieResponse.domainMapper.run { movies.map { it.reverseGet() }.respond() }
    }
  }
}