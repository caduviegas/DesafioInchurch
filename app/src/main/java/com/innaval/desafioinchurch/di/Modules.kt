package com.innaval.desafioinchurch.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.innaval.desafioinchurch.data.api.MovieApi
import com.innaval.desafioinchurch.data.api.RestApi
import com.innaval.desafioinchurch.data.data_sources.MovieLocalDataSource
import com.innaval.desafioinchurch.data.data_sources.MovieLocalDataSourceSharedPrefsImplementation
import com.innaval.desafioinchurch.data.data_sources.MovieLocalDataSourceSharedPrefsImplementation.Companion.SHARED_PREFERENCES_KEY
import com.innaval.desafioinchurch.data.data_sources.MovieRemoteDataSource
import com.innaval.desafioinchurch.data.data_sources.MovieRemoteDataSourceImplementation
import com.innaval.desafioinchurch.data.repositories.MovieRepositoryImplementation
import com.innaval.desafioinchurch.domain.repositories.MovieRepository
import com.innaval.desafioinchurch.domain.usecases.FavoriteOrDisfavoriteMovie
import com.innaval.desafioinchurch.domain.usecases.GetAllMovies
import com.innaval.desafioinchurch.domain.usecases.GetAllMoviesGenres
import com.innaval.desafioinchurch.domain.usecases.GetFavoriteMovies
import com.innaval.desafioinchurch.domain.usecases.SelectDetailMovie
import com.innaval.desafioinchurch.domain.usecases.ViewDetailMovie
import com.innaval.desafioinchurch.presentation.pages.favorite_movies.FavoriteMoviesViewModel
import com.innaval.desafioinchurch.presentation.pages.movie_detail.MovieDetailViewModel
import com.innaval.desafioinchurch.presentation.pages.movies.MoviesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules {

    val inChurchModule = module {
        single<MovieApi> { RestApi(androidContext()).getRetrofit().create(MovieApi::class.java) }
        single<SharedPreferences> {
            androidContext().getSharedPreferences(
                SHARED_PREFERENCES_KEY,
                Context.MODE_PRIVATE
            )
        }
        single<Gson> { Gson() }

        // single<MovieLocalDataSource>{MovieLocalDataSourceImplementation()}

        single<MovieLocalDataSource> { MovieLocalDataSourceSharedPrefsImplementation(get(), get()) }
        single<MovieRemoteDataSource> { MovieRemoteDataSourceImplementation(get<MovieApi>()) }

        single<MovieRepository> { MovieRepositoryImplementation(get(), get()) }

        factory<FavoriteOrDisfavoriteMovie> { FavoriteOrDisfavoriteMovie(get()) }
        factory<GetAllMovies> { GetAllMovies(get())}
        factory<GetAllMoviesGenres> { GetAllMoviesGenres(get())}
        factory<GetFavoriteMovies> { GetFavoriteMovies(get())}
        factory<SelectDetailMovie> { SelectDetailMovie(get())}
        factory<ViewDetailMovie> { ViewDetailMovie(get())}

        viewModel<MoviesViewModel>{ MoviesViewModel(get(), get(), get(), get())}
        viewModel<MovieDetailViewModel> { MovieDetailViewModel(get(), get(), get()) }
        viewModel<FavoriteMoviesViewModel> { FavoriteMoviesViewModel(get(), get()) }
    }
}