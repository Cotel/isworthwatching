package com.cotel.isworthwatching.movies.models

data class AddMovieRequest(
    val name: String = "",
    val releaseDate: Long = 0
)