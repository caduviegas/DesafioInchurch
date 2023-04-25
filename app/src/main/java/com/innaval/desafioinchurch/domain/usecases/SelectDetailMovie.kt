package com.innaval.desafioinchurch.domain.usecases

import com.innaval.desafioinchurch.domain.entities.Movie
import com.innaval.desafioinchurch.domain.repositories.MovieRepository

class SelectDetailMovie (private val movieRepository: MovieRepository) {

    suspend operator fun invoke(movie: Movie) {
        return movieRepository.cacheDetailMovie(movie)
    }
}