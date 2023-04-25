package com.innaval.desafioinchurch.presentation.pages.movies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.innaval.desafioinchurch.R
import com.innaval.desafioinchurch.core.utils.Response
import com.innaval.desafioinchurch.core.utils.Status
import com.innaval.desafioinchurch.databinding.ActivityMoviesBinding
import com.innaval.desafioinchurch.domain.entities.Movie
import com.innaval.desafioinchurch.presentation.pages.favorite_movies.FavoriteMoviesActivity
import com.innaval.desafioinchurch.presentation.pages.movie_detail.MovieDetailActivity
import com.innaval.desafioinchurch.presentation.widgets.InfiniteScrollListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesActivity : AppCompatActivity() {

    private val viewModel: MoviesViewModel by viewModel()
    private var movieAdapter: MovieAdapter? = null
    private lateinit var binding: ActivityMoviesBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showToolbar()
        showObserver()
        onClick()
    }

    private fun showToolbar() {
        binding.toolbar.setOnMenuItemClickListener {

            when (it?.itemId) {
                R.id.ic_bookmark -> {
                    val intent = Intent(this, FavoriteMoviesActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    private fun onClick() {
        binding.clError.setOnClickListener {
            viewModel.fetchAllMovies()
        }
    }

    private fun showObserver() {
        viewModel.getMovies().observe(this, Observer { response ->
            processResponse(response)
        })
        viewModel.getMoviesForUpdate().observe(this, Observer { response ->
            processMoviesForUpdateResponse(response)
        })
        viewModel.getSelectDetailMovieResponse().observe(this, Observer { response ->
            processOnSelectMovieResponse(response)
        })
        viewModel.getFavoriteOrDisfavoriteMovieResponse().observe(this, Observer { response ->
            processOnFavoriteOrDisfavoriteMovieResponse(response)
        })
    }

    private fun processOnSelectMovieResponse(response: Response) {
        if (response.status == Status.SUCCESS) {
            if (response.data != null) {
                val intent = Intent(this, MovieDetailActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun processOnFavoriteOrDisfavoriteMovieResponse(response: Response) {
        if (response.status == Status.SUCCESS) {
            if (response.data != null) {
                movieAdapter?.updateItem(response.data as Int)
            }
        }
    }

    private fun processMoviesForUpdateResponse(response: Response) {
        when (response.status) {
            Status.SUCCESS -> showUpdateMovies(response.data)
            else -> {
            }
        }
    }

    private fun processResponse(response: Response) {
        when (response.status) {
            Status.LOADING -> showLoading()
            Status.SUCCESS -> showMovies(response.data)
            Status.EMPTY_RESPONSE -> {
            }

            Status.ERROR -> showError(response.error)
        }
    }

    private fun showLoading() {
        if (movieAdapter == null) {
            binding.pgLoading.visibility = View.VISIBLE
            binding.rvMovies.visibility = View.GONE
            binding.clError.visibility = View.GONE
        }
    }

    private fun showMovies(data: Any?) {
        binding.pgLoading.visibility = View.GONE
        binding.rvMovies.visibility = View.VISIBLE

        if (data is List<*>) {

            if (movieAdapter == null) {
                movieAdapter = MovieAdapter(
                    data.filterIsInstance<Movie>().toMutableList(),
                    ::onItemClick,
                    ::onFavoriteClick
                )

                val layoutManager = GridLayoutManager(this, 2)
                binding.rvMovies.layoutManager = layoutManager
                binding.rvMovies.addOnScrollListener(
                    InfiniteScrollListener({
                        viewModel.loadMoreMovies()
                    }, layoutManager)
                )
                binding.rvMovies.adapter = movieAdapter
            } else {
                movieAdapter?.addItems(data.filterIsInstance<Movie>().toMutableList())
            }
        }
    }

    private fun showError(throwable: Throwable?) {

        if (movieAdapter == null) {
            binding.pgLoading.visibility = View.GONE
            binding.tvError.text = throwable?.message
            binding.clError.visibility = View.VISIBLE
        }
    }

    private fun showUpdateMovies(data: Any?) {
        if (data is List<*>) {
            movieAdapter?.updateAllItems(data.filterIsInstance<Movie>().toMutableList())
        }
    }

    private fun onItemClick(movie: Movie) {
        viewModel.selectDetailMovie(movie)
    }

    private fun onFavoriteClick(movie: Movie, pos: Int) {
        viewModel.favoriteOrDisfavorMovie(movie, pos)
    }

    override fun onResume() {
        super.onResume()
        if (this.movieAdapter != null)
            viewModel.updateMovies(this.movieAdapter?.movieList() ?: mutableListOf())
    }


}