package com.innaval.desafioinchurch.data.data_sources

import com.innaval.desafioinchurch.data.models.GenreResultModel
import com.innaval.desafioinchurch.data.models.MovieModel
import com.innaval.desafioinchurch.data.models.PageModel

interface MovieRemoteDataSource {

    suspend fun getAllMovies(page: Int = 1): PageModel<MovieModel>
    suspend fun getAllMoviesGenres(): GenreResultModel
}