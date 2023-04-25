package com.innaval.desafioinchurch.data.data_sources

import com.innaval.desafioinchurch.data.models.MovieModel

object InCacheDao {
    private var movieDetail: MovieModel? = null
    fun getCachedMovie(): MovieModel? = this.movieDetail
    fun cacheMovie(movie: MovieModel){
        this.movieDetail = movie
    }
    
    private var favoriteMovies = mutableListOf<MovieModel>()
    fun getCachedFavoriteMovies() = favoriteMovies
    fun setCachedFavoriteMovies(list: MutableList<MovieModel>){
        this.favoriteMovies = list
    }
}