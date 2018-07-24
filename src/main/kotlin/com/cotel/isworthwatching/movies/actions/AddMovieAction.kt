package com.cotel.isworthwatching.movies.actions

import arrow.core.toOption
import arrow.data.mapOf
import arrow.effects.IO
import com.cotel.isworthwatching.base.Responder
import com.cotel.isworthwatching.base.creationSuccessResponder
import com.cotel.isworthwatching.base.errorResponder
import com.cotel.isworthwatching.movies.command.AddMovie
import com.cotel.isworthwatching.movies.command.AddMovieCommand
import com.cotel.isworthwatching.movies.models.AddMovieRequest
import com.cotel.isworthwatching.movies.command.AddMovieUseCase
import com.cotel.isworthwatching.movies.models.Movie
import com.cotel.isworthwatching.movies.models.MovieEntity
import com.cotel.isworthwatching.movies.MoviesRepository
import com.cotel.isworthwatching.movies.command.IsMovieRepeated
import com.cotel.isworthwatching.movies.models.MovieResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/movies")
class AddMovieAction(private val repository: MoviesRepository) {

  @PostMapping
  fun invoke(@RequestBody addMovieRequest: AddMovieRequest): ResponseEntity<*> {
    return object : AddMovieUseCase {
      override val addMovie: AddMovie = {
        IO {
          val entity = MovieEntity.addRequestMapper.run { it.reverseGet() }
          val result = repository.save(entity)
          MovieEntity.domainMapper.run { result.get() }
        }
      }

      override val isMovieRepeated: IsMovieRepeated = {
        IO {
          val entity = MovieEntity.addRequestMapper.run { it.reverseGet() }
          repository.findOneBySearchName(entity.searchName).toOption().isDefined()
        }
      }
    }.run {
      val result = AddMovieCommand(addMovieRequest).runUseCase().unsafeRunSync()
      result.fold(
          { errorResponder(HttpStatus.CONFLICT).run {
            mapOf("error" to "A Movie with name ${addMovieRequest.name} already exists").respond()
          } },
          {
            with (MovieResponse.domainMapper) {
              creationSuccessResponder<MovieResponse>().run { it.reverseGet().respond() }
            }
          }
      )
    }
  }

}