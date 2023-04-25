package com.innaval.desafioinchurch.data.data_sources

import com.innaval.desafioinchurch.core.exceptions.ResourceNotFoundException
import com.innaval.desafioinchurch.data.models.MovieModel

class MovieLocalDataSourceImplementation : MovieLocalDataSource {

    override suspend fun cacheDetailMovie(movie: MovieModel) {
        InCacheDao.cacheMovie(movie)
    }

    override suspend fun getCacheDetailMovie(): MovieModel {
        return InCacheDao.getCachedMovie() ?: throw ResourceNotFoundException()
    }

    override suspend fun cacheFavoriteMovies(movies: MutableList<MovieModel>) {
        InCacheDao.setCachedFavoriteMovies(movies.toMutableList())
    }

    override suspend fun getCachedFavoriteMovies(filter: String): MutableList<MovieModel> {
        return InCacheDao.getCachedFavoriteMovies().filter {
            it.title.contains(filter, ignoreCase = true)
        }.toMutableList()
    }
}