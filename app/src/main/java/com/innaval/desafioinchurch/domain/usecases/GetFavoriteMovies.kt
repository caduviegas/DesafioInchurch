package com.innaval.desafioinchurch.domain.usecases

import com.innaval.desafioinchurch.domain.entities.Movie
import com.innaval.desafioinchurch.domain.repositories.MovieRepository

class GetFavoriteMovies(private val repository: MovieRepository) {

    suspend operator fun invoke(filter: String = ""): List<Movie> {
        return repository.getFavoriteMovies(filter)
    }
}