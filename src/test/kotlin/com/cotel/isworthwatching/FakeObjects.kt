package com.cotel.isworthwatching

import com.cotel.isworthwatching.movies.models.Movie
import java.util.*

fun fakeMovie() = Movie(
    id = "test",
    name = "test",
    releaseDate = Date(),
    positiveVotes = 0,
    negativeVotes = 0
)