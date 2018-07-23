package com.cotel.isworthwatching.movies.models

import java.util.*

data class Movie(
    val id: String,
    val name: String,
    val releaseDate: Date,
    val positiveVotes: Int,
    val negativeVotes: Int
)