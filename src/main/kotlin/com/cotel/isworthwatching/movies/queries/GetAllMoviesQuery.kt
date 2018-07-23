package com.cotel.isworthwatching.movies.queries

import arrow.effects.IO
import com.cotel.isworthwatching.movies.models.Movie

typealias GetAllMovies = (Int, Int) -> IO<List<Movie>>

data class GetAllMoviesQuery(val page: Int, val pageSize: Int)

interface GetAllMoviesUseCase {

  val query: GetAllMovies

  fun GetAllMoviesQuery.runUseCase(): IO<List<Movie>> = query(page, pageSize)

}