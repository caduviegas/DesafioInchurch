package com.innaval.desafioinchurch.presentation.pages.favorite_movies

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import  androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.innaval.desafioinchurch.R
import com.innaval.desafioinchurch.core.utils.Response
import com.innaval.desafioinchurch.core.utils.Status
import com.innaval.desafioinchurch.databinding.ActivityFavoriteMoviesBinding
import com.innaval.desafioinchurch.domain.entities.Movie
import com.innaval.desafioinchurch.presentation.pages.movie_detail.MovieDetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteMoviesActivity : AppCompatActivity() {

    private val viewModel: FavoriteMoviesViewModel by viewModel()
    private var favoriteMoviesAdapter: FavoriteMoviesAdapter? = null
    private var searchView: SearchView? = null
    private lateinit var binding: ActivityFavoriteMoviesBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showObservers()
        showSearchView()
    }

    private fun showObservers() {
        viewModel.movies().observe(this, Observer { response -> processResponse(response) })
        viewModel.selectDetailMovieResponse()
            .observe(this, Observer { response -> processOnSelectMovieResponse(response) })
    }

    private fun showSearchView() {
        val searchItem: MenuItem? = binding.toolbar.menu?.findItem(R.id.ic_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView? = searchItem?.actionView as SearchView

        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))


        searchView?.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false)
            queryHint = "Search..."
            inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS

            val closeButton =
                findViewById<ImageView>(com.google.android.material.R.id.search_close_btn)
            closeButton.setOnClickListener {
                setQuery(null, false)
                clearFocus()
                viewModel.searchMovies("")
            }

            val queryTextListener = object : SearchView.OnQueryTextListener {

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.searchMovies(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }
            }
            this.setOnQueryTextListener(queryTextListener)
        }
        true
    }

    private fun processOnSelectMovieResponse(response: Response) {
        if (response.status == Status.SUCCESS) {
            if (response.data != null) {
                val intent = Intent(this, MovieDetailActivity::class.java)
                startActivity(intent)
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
        binding.tvError.visibility = View.GONE
        if (favoriteMoviesAdapter == null)
            binding.pgLoading.visibility = View.VISIBLE
    }

    private fun showMovies(data: Any?) {

        binding.pgLoading.visibility = View.GONE
        binding.tvError.visibility = View.GONE

        if (data is List<*>) {

            if (data.isEmpty()) {
                showError(Throwable("Nenhum filme favorito encontrado"))
                favoriteMoviesAdapter?.updateAllItems(mutableListOf())
                return
            }

            if (favoriteMoviesAdapter == null) {
                favoriteMoviesAdapter =
                    FavoriteMoviesAdapter(
                        data.filterIsInstance<Movie>().toMutableList(),
                        ::onItemClick
                    )

                val layoutManager = LinearLayoutManager(this)
                binding.rvMovies.layoutManager = layoutManager
                binding.rvMovies.adapter = favoriteMoviesAdapter
            } else {
                favoriteMoviesAdapter?.updateAllItems(
                    data.filterIsInstance<Movie>().toMutableList()
                )
            }
        }
    }

    private fun showError(throwable: Throwable?) {
        binding.pgLoading.visibility = View.GONE
        binding.tvError.text = throwable?.message
        binding.tvError.visibility = View.VISIBLE
    }

    private fun onItemClick(movie: Movie) {
        viewModel.selectDetailMovie(movie)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchFavoriteMovies()
    }
}