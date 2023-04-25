package com.innaval.desafioinchurch.presentation.pages.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.innaval.desafioinchurch.core.utils.Response
import com.innaval.desafioinchurch.domain.entities.Movie
import com.innaval.desafioinchurch.domain.usecases.FavoriteOrDisfavoriteMovie
import com.innaval.desafioinchurch.domain.usecases.GetAllMovies
import com.innaval.desafioinchurch.domain.usecases.GetFavoriteMovies
import com.innaval.desafioinchurch.domain.usecases.SelectDetailMovie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesViewModel(
    val favoriteOrDisfavoriteMovieUseCase: FavoriteOrDisfavoriteMovie,
    val selectDetailMovieUseCase: SelectDetailMovie,
    val getAllMovies: GetAllMovies,
    val getFavoriteMoviesUseCase: GetFavoriteMovies
) : ViewModel() {

    private var movies: MutableLiveData<Response> = MutableLiveData()
    fun getMovies(): LiveData<Response> = movies

    private var moviesForUpdate: MutableLiveData<Response> = MutableLiveData()
    fun getMoviesForUpdate(): LiveData<Response> = moviesForUpdate

    private var selectDetailMovieResponse: MutableLiveData<Response> = MutableLiveData()
    fun getSelectDetailMovieResponse(): LiveData<Response> = selectDetailMovieResponse

    private var favoriteOrDisfavorMovieResponse: MutableLiveData<Response> = MutableLiveData()
    fun getFavoriteOrDisfavoriteMovieResponse(): LiveData<Response> =
        favoriteOrDisfavorMovieResponse

    private var currentPage = 1

    init {
        fetchAllMovies()
    }

    fun fetchAllMovies() {
        movies.postValue(Response.loading())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = getAllMovies(currentPage)
                movies.postValue(Response.success(result))
            } catch (t: Throwable) {
                movies.postValue(Response.error(t))
            }
        }
    }

    fun loadMoreMovies() {
        currentPage += 1
        fetchAllMovies()
    }

    fun updateMovies(movies: MutableList<Movie>) {

        moviesForUpdate.postValue(Response.loading())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val favoriteMovies = getFavoriteMoviesUseCase()
                val favoriteMoviesIDs = favoriteMovies.map { it.id }
                movies.forEach {
                    it.isFavorite = favoriteMoviesIDs.contains(it.id)
                }
                moviesForUpdate.postValue(Response.success(movies))
            } catch (t: Throwable) {
                moviesForUpdate.postValue(Response.error(t))
            }
        }
    }

    fun selectDetailMovie(movie: Movie) {
        selectDetailMovieResponse.postValue(Response.loading())
        CoroutineScope(Dispatchers.IO).launch {
            selectDetailMovieUseCase(movie)
            selectDetailMovieResponse.postValue(Response.success(true))
        }
    }

    fun favoriteOrDisfavorMovie(movie: Movie, pos: Int) {
        favoriteOrDisfavorMovieResponse.postValue(Response.loading())
        CoroutineScope(Dispatchers.IO).launch {
            favoriteOrDisfavoriteMovieUseCase(movie)
            favoriteOrDisfavorMovieResponse.postValue(Response.success(pos))
        }
    }

    override fun onCleared() {
        super.onCleared()
        selectDetailMovieResponse.postValue(Response.empty())
    }


}