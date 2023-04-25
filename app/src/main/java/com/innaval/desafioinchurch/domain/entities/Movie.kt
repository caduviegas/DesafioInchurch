package com.innaval.desafioinchurch.domain.entities

import java.util.Date

data class Movie(
    val id: Int,
    val originalTitle: String,
    val originalLanguage: String,
    val title: String,
    val posterPath: String,
    val isAdult: Boolean,
    val overview: String,
    val releaseDate: Date,
    val popularity: Double,
    val voteCount: Int,
    val video: Boolean,
    val voteAverage: Double,
    val backdropPath: String,
    var genreIds: List<Int>
) {
    var isFavorite: Boolean = false

    fun getGenres(allGenres: List<Genre>): List<Genre> {
        return allGenres.filter {
            genreIds.contains(it.id)
        }
    }
}