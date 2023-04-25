package com.innaval.desafioinchurch.domain.usecases

import com.innaval.desafioinchurch.domain.entities.Genre
import com.innaval.desafioinchurch.domain.repositories.MovieRepository

class GetAllMoviesGenres(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(): List<Genre>{
        return movieRepository.getAllMoviesGenres()
    }
}