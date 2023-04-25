package com.innaval.desafioinchurch.presentation.pages.favorite_movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.innaval.desafioinchurch.core.utils.Response
import com.innaval.desafioinchurch.domain.entities.Movie
import com.innaval.desafioinchurch.domain.usecases.GetFavoriteMovies
import com.innaval.desafioinchurch.domain.usecases.SelectDetailMovie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavoriteMoviesViewModel(
    val getFavoriteMoviesUseCase: GetFavoriteMovies,
    val selectDetailMovieUse: SelectDetailMovie
) : ViewModel() {

    private var movies: MutableLiveData<Response> = MutableLiveData()
    fun movies() = movies

    private var selectDetailMovieResponse: MutableLiveData<Response> = MutableLiveData()
    fun selectDetailMovieResponse() = selectDetailMovieResponse

    private var filter = ""

    fun fetchFavoriteMovies() {

        movies.postValue(Response.loading())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = getFavoriteMoviesUseCase(filter)
                movies.postValue(Response.success(result))
            } catch (e: Exception) {
                movies.postValue(Response.error(e))
            }
        }
    }

    fun searchMovies(filter: String) {
        this.filter = filter
        fetchFavoriteMovies()
    }

    fun selectDetailMovie(movie: Movie) {
        selectDetailMovieResponse.postValue(Response.loading())
        CoroutineScope(Dispatchers.IO).launch {
            selectDetailMovieUse(movie)
            selectDetailMovieResponse.postValue(Response.success(true))
        }
    }
}