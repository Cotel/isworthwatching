package com.cotel.isworthwatching.movies

import com.cotel.isworthwatching.movies.models.MovieEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface MoviesRepository : CrudRepository<MovieEntity, String> {

  fun findAll(pageable: Pageable): Page<MovieEntity>

  fun findOneById(id: String): MovieEntity?

  fun findOneBySearchName(searchName: String): MovieEntity?

}