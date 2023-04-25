package com.innaval.desafioinchurch.domain.usecases

import com.innaval.desafioinchurch.domain.entities.Movie
import com.innaval.desafioinchurch.domain.repositories.MovieRepository

class FavoriteOrDisfavoriteMovie( private val repository: MovieRepository) {

    suspend operator fun invoke(movie: Movie){
        repository.favoriteOrDisfavorMovie(movie)
    }
}