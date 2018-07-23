package com.cotel.isworthwatching.movies.actions

import com.cotel.isworthwatching.base.Responder
import com.cotel.isworthwatching.base.defaultNotFoundResponder
import com.cotel.isworthwatching.base.entityResponder
import com.cotel.isworthwatching.base.notFoundResponder
import com.cotel.isworthwatching.movies.MoviesRepository
import com.cotel.isworthwatching.movies.command.UpdateMovie
import com.cotel.isworthwatching.movies.command.VoteCommand
import com.cotel.isworthwatching.movies.command.VoteType
import com.cotel.isworthwatching.movies.command.VoteUseCase
import com.cotel.isworthwatching.movies.models.Movie
import com.cotel.isworthwatching.movies.queries.GetMovie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/movies")
class PositiveVoteAction(private val moviesRepository: MoviesRepository) :
    Responder<Movie> by entityResponder() {
  @PutMapping("/{id}/positiveVote")
  fun invoke(@PathVariable("id") id: String): ResponseEntity<*> {
    return object : VoteUseCase {
      override val voteType: VoteType = VoteType.PositiveVote
      override val getMovie: GetMovie = getMovieDependency(moviesRepository)
      override val positiveVote: UpdateMovie = updateMovieDependency(moviesRepository)
    }.run {
      val result = VoteCommand(id).runUseCase().unsafeRunSync()

      result.fold(
          { defaultNotFoundResponder(Movie::class.java, id) },
          { it.respond() }
      )
    }
  }
}