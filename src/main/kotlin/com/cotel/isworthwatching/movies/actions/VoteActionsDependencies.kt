package com.cotel.isworthwatching.movies.actions

import arrow.core.toOption
import arrow.effects.IO
import com.cotel.isworthwatching.movies.MoviesRepository
import com.cotel.isworthwatching.movies.command.UpdateMovie
import com.cotel.isworthwatching.movies.models.MovieEntity
import com.cotel.isworthwatching.movies.queries.GetMovie

val getMovieDependency: (MoviesRepository) -> GetMovie = { moviesRepository ->
  {
    IO {
      moviesRepository.findOneById(it)
          .toOption()
          .map { MovieEntity.domainMapper.run { it.get() } }
    }
  }
}

val updateMovieDependency: (MoviesRepository) -> UpdateMovie = { moviesRepository ->
  {
    IO {
      val domainMapper = MovieEntity.domainMapper
      val entity = domainMapper.run { it.reverseGet() }
      domainMapper.run { moviesRepository.save(entity).get() }
    }
  }
}