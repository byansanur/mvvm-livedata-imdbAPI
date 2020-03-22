package com.byandev.movieapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.byandev.movieapp.R
import com.byandev.movieapp.data.api.IMAGE_BASE_URL
import com.byandev.movieapp.data.api.ApiEndPoint
import com.byandev.movieapp.data.api.RetrofitClient
import com.byandev.movieapp.data.model.MovieDetails
import com.byandev.movieapp.data.source.NetworkState
import com.byandev.movieapp.ui.repo.MovieDetailRepository
import com.byandev.movieapp.ui.viewModel.DetailActivityViewModel
import kotlinx.android.synthetic.main.activity_detail_movie.*
import java.text.NumberFormat
import java.util.*

class DetailMovieActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailActivityViewModel
    private lateinit var detailRepository: MovieDetailRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        val movieId: Int = intent.getIntExtra("id",1)

        val apiService : ApiEndPoint = RetrofitClient.getClient()
        detailRepository = MovieDetailRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {
            bindingUi(it)
        })

        viewModel.networkState.observe(this, Observer {
            if (it == NetworkState.LOADING)
                progress_bar.visibility = View.VISIBLE
            else if (it == NetworkState.ERROR)
                txt_error.visibility = View.VISIBLE
            else
                progress_bar.visibility = View.GONE
                txt_error.visibility = View.GONE

        })

    }

    private fun bindingUi(it: MovieDetails) {

        supportActionBar?.title = it.title

        movie_title.text = it.title
        movie_tagline.text = it.tagline
        movie_release_date.text = it.releaseDate
        movie_rating.text = it.rating.toString()
        movie_runtime.text = it.runtime.toString()
        movie_overview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text = formatCurrency.format(it.budget)
        movie_revenue.text = formatCurrency.format(it.revenue)

        val imgURL = IMAGE_BASE_URL + it.posterPath
        Glide.with(this)
            .load(imgURL)
            .error(R.drawable.poster_placeholder)
            .into(iv_movie_poster)
    }

    private fun getViewModel(movieId: Int): DetailActivityViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DetailActivityViewModel(detailRepository,movieId) as T
            }
        })[DetailActivityViewModel::class.java]
    }
}
