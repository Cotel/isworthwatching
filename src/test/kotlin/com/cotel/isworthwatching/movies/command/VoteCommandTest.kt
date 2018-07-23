package com.cotel.isworthwatching.movies.command

import arrow.core.Option
import arrow.core.toOption
import arrow.effects.IO
import com.cotel.isworthwatching.fakeMovie
import com.cotel.isworthwatching.movies.queries.GetMovie
import io.kotlintest.assertions.arrow.option.shouldBeNone
import io.kotlintest.assertions.arrow.option.shouldBeSome
import io.kotlintest.specs.StringSpec

class VoteCommandTest : StringSpec() {

  init {

    "If vote type is positive it should return the movie voted positively" {
      val movie = fakeMovie()
      val useCase = object : VoteUseCase {


        override val voteType: VoteType = VoteType.PositiveVote
        override val getMovie: GetMovie = { IO.just(movie.toOption()) }
        override val positiveVote: UpdateMovie = { IO.just(movie.copy(positiveVotes = movie.positiveVotes + 1)) }
      }.test().unsafeRunSync()

      useCase.shouldBeSome(movie.copy(positiveVotes = 1))
    }

    "If vote type is negative it should return the movie voted negatively" {
      val movie = fakeMovie()
      val useCase = object : VoteUseCase {


        override val voteType: VoteType = VoteType.NegativeVote
        override val getMovie: GetMovie = { IO.just(movie.toOption()) }
        override val positiveVote: UpdateMovie = { IO.just(movie.copy(negativeVotes = movie.negativeVotes + 1)) }
      }.test().unsafeRunSync()

      useCase.shouldBeSome(movie.copy(negativeVotes = 1))
    }

    "If no movie is found it should return None" {
      val movie = fakeMovie()
      val useCase = object : VoteUseCase {


        override val voteType: VoteType = VoteType.PositiveVote
        override val getMovie: GetMovie = { IO.just(Option.empty()) }
        override val positiveVote: UpdateMovie = { IO.just(movie.copy(positiveVotes = movie.positiveVotes + 1)) }
      }.test().unsafeRunSync()

      useCase.shouldBeNone()
    }

  }

  private fun VoteUseCase.test() =
      VoteCommand("x").runUseCase()

}