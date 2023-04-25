package com.innaval.desafioinchurch.data.data_sources

import com.innaval.desafioinchurch.data.models.MovieModel

interface MovieLocalDataSource {
    suspend fun cacheDetailMovie(movie: MovieModel)
    suspend fun getCacheDetailMovie(): MovieModel

    suspend fun cacheFavoriteMovies(movies: MutableList<MovieModel>)
    suspend fun getCachedFavoriteMovies(filter: String = ""): MutableList<MovieModel>
}