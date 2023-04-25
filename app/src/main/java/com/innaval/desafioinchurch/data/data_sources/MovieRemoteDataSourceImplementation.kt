package com.innaval.desafioinchurch.data.data_sources

import com.innaval.desafioinchurch.BuildConfig
import com.innaval.desafioinchurch.core.exceptions.InvalidApiKeyException
import com.innaval.desafioinchurch.core.exceptions.ResourceNotFoundException
import com.innaval.desafioinchurch.data.api.MovieApi
import com.innaval.desafioinchurch.data.models.GenreResultModel
import com.innaval.desafioinchurch.data.models.MovieModel
import com.innaval.desafioinchurch.data.models.PageModel

class MovieRemoteDataSourceImplementation(private val movieApi: MovieApi): MovieRemoteDataSource {
    override suspend fun getAllMovies(page: Int): PageModel<MovieModel> {
        val response = movieApi.getAllMovies(BuildConfig.API_KEY, page)
        if(response.isSuccessful){
            return response.body()!!
        } else{
            if(response.code() == 401)
                throw InvalidApiKeyException()
            else if(response.code() == 404)
                throw ResourceNotFoundException()

            throw Exception()
        }
    }

    override suspend fun getAllMoviesGenres(): GenreResultModel {
        val response = movieApi.getAllMoviesGenres(BuildConfig.API_KEY)
        if(response.isSuccessful){
            return response.body()!!
        } else {
            if(response.code() == 401)
                throw InvalidApiKeyException()
            else if(response.code() == 404)
                throw ResourceNotFoundException()

            throw Exception()
        }
    }
}