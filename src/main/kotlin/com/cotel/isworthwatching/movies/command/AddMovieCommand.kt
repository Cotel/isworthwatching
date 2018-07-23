package com.cotel.isworthwatching.movies.command

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.effects.ForIO
import arrow.effects.IO
import arrow.effects.extensions
import arrow.effects.fix
import arrow.instances.ForIor
import arrow.typeclasses.binding
import com.cotel.isworthwatching.movies.models.AddMovieRequest
import com.cotel.isworthwatching.movies.models.Movie

typealias AddMovie = (AddMovieRequest) -> IO<Movie>

typealias IsMovieRepeated = (AddMovieRequest) -> IO<Boolean>

data class AddMovieCommand(val data: AddMovieRequest)

object MovieIsRepeatedError

interface AddMovieUseCase {
  val addMovie: AddMovie
  val isMovieRepeated: IsMovieRepeated

  fun AddMovieCommand.runUseCase(): IO<Either<MovieIsRepeatedError, Movie>> {
    val cmd = this
    return ForIO extensions {
      binding {
        val isRepeated = isMovieRepeated(cmd.data).bind()

        if (isRepeated) {
          MovieIsRepeatedError.left()
        } else {
          addMovie(cmd.data).bind().right()
        }

      }.fix()
    }
  }
}