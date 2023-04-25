package com.innaval.desafioinchurch.domain.usecases

import com.innaval.desafioinchurch.domain.entities.Movie
import com.innaval.desafioinchurch.domain.repositories.MovieRepository

class ViewDetailMovie(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(): Movie {
        return movieRepository.getCachedDetailMovie()
    }
}