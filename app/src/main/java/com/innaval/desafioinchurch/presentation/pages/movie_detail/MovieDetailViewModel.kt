package com.innaval.desafioinchurch.presentation.pages.movie_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.innaval.desafioinchurch.core.utils.Response
import com.innaval.desafioinchurch.domain.entities.Movie
import com.innaval.desafioinchurch.domain.usecases.FavoriteOrDisfavoriteMovie
import com.innaval.desafioinchurch.domain.usecases.GetAllMoviesGenres
import com.innaval.desafioinchurch.domain.usecases.ViewDetailMovie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MovieDetailViewModel(
    val favoriteOrDisfavoriteMovieUseCase: FavoriteOrDisfavoriteMovie,
    val viewDetailMovie: ViewDetailMovie,
    val getAllMoviesGenres: GetAllMoviesGenres
) : ViewModel() {

    private var genres: MutableLiveData<Response> = MutableLiveData()
    fun genres() = genres

    private var detailMovie: MutableLiveData<Response> = MutableLiveData()
    fun detailMovie() = detailMovie

    private var favoriteOrDisfavoriteResponse: MutableLiveData<Response> = MutableLiveData()
    fun favoriteOrDisfavoriteMovieResponse() = favoriteOrDisfavoriteResponse

    init {
        fetchDetailMovie()
        fetchAllGenres()
    }

    private fun fetchAllGenres() {
        genres.value = Response.loading()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = getAllMoviesGenres()
                genres.postValue(Response.success(result))
            } catch (e: Exception) {
                genres.postValue(Response.error(e))
            }
        }
    }

    private fun fetchDetailMovie() {

        detailMovie.value = Response.loading()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = viewDetailMovie()
                detailMovie.postValue(Response.success(result))
            } catch (e: Exception) {
                detailMovie.postValue(Response.error(e))
            }
        }
    }

    fun favoriteOrDisfavoriteMovie() {

        if (detailMovie.value != null && detailMovie.value?.data is Movie) {

            val movie = detailMovie.value?.data as Movie
            favoriteOrDisfavoriteResponse.postValue(Response.loading())
            CoroutineScope(Dispatchers.IO).launch {
                favoriteOrDisfavoriteMovieUseCase(movie)
                favoriteOrDisfavoriteResponse.postValue(Response.success(Unit))
            }
        }
    }
}