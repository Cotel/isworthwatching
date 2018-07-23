package com.cotel.isworthwatching.movies.queries

import arrow.core.Option
import arrow.effects.ForIO
import arrow.effects.IO
import arrow.effects.extensions
import arrow.effects.fix
import arrow.typeclasses.binding
import com.cotel.isworthwatching.movies.models.Movie

typealias GetMovie = (String) -> IO<Option<Movie>>

data class GetMovieQuery(val id: String)

interface GetMovieUseCase {

  val getMovie: GetMovie

  fun GetMovieQuery.runUseCase(): IO<Option<Movie>> = getMovie(id)

}
