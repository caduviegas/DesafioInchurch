package com.innaval.desafioinchurch.presentation.pages.movie_detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.innaval.desafioinchurch.BuildConfig
import com.innaval.desafioinchurch.R
import com.innaval.desafioinchurch.core.utils.Response
import com.innaval.desafioinchurch.core.utils.Status
import com.innaval.desafioinchurch.databinding.ActivityMovieDetailBinding
import com.innaval.desafioinchurch.domain.entities.Genre
import com.innaval.desafioinchurch.domain.entities.Movie
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat

class MovieDetailActivity : AppCompatActivity() {

    private val viewModel: MovieDetailViewModel by viewModel()
    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showObserver()
        binding.toolbar.title = ""
    }

    private fun showObserver() {
        viewModel.detailMovie().observe(this, Observer { response -> processResponse(response) })
        viewModel.genres().observe(this, Observer { response -> processResponse(response) })
        viewModel.favoriteOrDisfavoriteMovieResponse()
            .observe(this, Observer { response -> processResponse(response) })
    }

    private fun processResponse(response: Response) {
        when (response.status) {
            Status.LOADING -> showLoading()
            Status.SUCCESS -> showMovie(response.data)
            Status.EMPTY_RESPONSE -> {

            }
            Status.ERROR -> showError(response.error)
        }
    }

    private fun showLoading() {
        binding.pgLoading.visibility = View.VISIBLE
    }

    private fun showMovie(data: Any?) {

        if (viewModel.detailMovie().value != null &&
            viewModel.detailMovie().value?.status == Status.SUCCESS &&
            viewModel.genres().value != null &&
            viewModel.genres().value?.status == Status.SUCCESS
        ) {
            binding.pgLoading.visibility = View.GONE
            this.fillScreen(
                (viewModel.genres().value?.data as List<*>).filterIsInstance<Genre>(),
                viewModel.detailMovie().value?.data as Movie
            )
        }
    }

    private fun showError(throwable: Throwable?) {
        binding.pgLoading.visibility = View.GONE
        Toast.makeText(applicationContext, throwable?.message, Toast.LENGTH_SHORT).show()
        onBackPressed()
    }

    private fun fillScreen(genres: List<Genre>, movie: Movie) {
        binding.toolbar.title = movie.title

        Picasso.with(this)
            .load("${BuildConfig.IMAGE_URL}${movie.backdropPath}").fit()
            .into(binding.ivPoster)

        binding.tvTitle.text = movie.title
        binding.tvReleaseDate.text = SimpleDateFormat("dd/MM/yyyy").format(movie.releaseDate)
        binding.tvReleaseDate.text = movie.getGenres(genres).joinToString { it.name }
        binding.tvOverview.text = movie.overview

        binding.toolbar.menu.findItem(R.id.ic_favor_disfavor)?.setIcon(
            if (movie.isFavorite)
                R.drawable.ic_star_white_24dp
            else
                R.drawable.ic_star_border_white_24dp
        )
        binding.toolbar.setOnMenuItemClickListener {
            when (it?.itemId) {
                R.id.ic_favor_disfavor -> {
                    viewModel.favoriteOrDisfavoriteMovie()
                    true
                }
                else -> false
            }
        }
    }
}