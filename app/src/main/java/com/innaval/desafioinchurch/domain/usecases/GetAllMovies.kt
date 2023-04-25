package com.innaval.desafioinchurch.domain.usecases

import com.innaval.desafioinchurch.domain.entities.Movie
import com.innaval.desafioinchurch.domain.repositories.MovieRepository

class GetAllMovies(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(page: Int = 1): List<Movie> {
        return movieRepository.getAllMovies(page)
    }
}