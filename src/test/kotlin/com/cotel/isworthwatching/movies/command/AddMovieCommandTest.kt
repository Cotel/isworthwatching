package com.cotel.isworthwatching.movies.command

import arrow.effects.IO
import com.cotel.isworthwatching.fakeMovie
import com.cotel.isworthwatching.movies.models.AddMovieRequest
import com.cotel.isworthwatching.movies.models.Movie
import io.kotlintest.assertions.arrow.either.shouldBeLeft
import io.kotlintest.assertions.arrow.either.shouldBeRight
import io.kotlintest.specs.StringSpec
import java.util.*

class AddMovieCommandTest : StringSpec() {
  init {

    "If movie is not repeated result should be okay" {
      val useCase = object : AddMovieUseCase {
        override val addMovie: AddMovie = { IO.just(fakeMovie()) }
        override val isMovieRepeated: IsMovieRepeated = { _ -> IO.just(false) }
      }.test().unsafeRunSync()

      useCase.shouldBeRight()
    }

    "If movie is repeated result should be RepeatedMovieError" {
      val useCase = object : AddMovieUseCase {
        override val addMovie: AddMovie = { IO.just(fakeMovie()) }
        override val isMovieRepeated: IsMovieRepeated = { _ -> IO.just(true) }
      }.test().unsafeRunSync()

      useCase.shouldBeLeft(MovieIsRepeatedError)
    }

  }

  private fun AddMovieUseCase.test() =
      AddMovieCommand(AddMovieRequest()).runUseCase()
}