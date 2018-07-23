package com.cotel.isworthwatching.movies.command

import arrow.core.Option
import arrow.core.toOption
import arrow.data.OptionT
import arrow.data.value
import arrow.effects.IO
import arrow.effects.fix
import arrow.effects.monad
import arrow.instances.ForOptionT
import arrow.typeclasses.binding
import com.cotel.isworthwatching.movies.models.Movie
import com.cotel.isworthwatching.movies.queries.GetMovie

typealias UpdateMovie = (Movie) -> IO<Movie>

data class VoteCommand(val id: String)

sealed class VoteType {
  object PositiveVote : VoteType()
  object NegativeVote : VoteType()
}

interface VoteUseCase {

  val voteType: VoteType
  val getMovie: GetMovie
  val positiveVote: UpdateMovie

  fun VoteCommand.runUseCase(): IO<Option<Movie>> {
    val command = this
    return ForOptionT(IO.monad()) extensions {
      binding {
        val detail = OptionT(getMovie(command.id)).bind()

        val votedMovie = when (voteType) {
          is VoteType.PositiveVote -> detail.copy(positiveVotes = detail.positiveVotes + 1)
          is VoteType.NegativeVote -> detail.copy(negativeVotes = detail.negativeVotes + 1)
        }

        OptionT(positiveVote(votedMovie).map { it.toOption() }).bind()
      }.value().fix()
    }
  }
}
