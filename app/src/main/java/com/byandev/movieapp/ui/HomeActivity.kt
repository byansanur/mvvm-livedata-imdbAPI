package com.byandev.movieapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.byandev.movieapp.R
import com.byandev.movieapp.data.api.ApiEndPoint
import com.byandev.movieapp.data.api.RetrofitClient
import com.byandev.movieapp.data.source.NetworkState
import com.byandev.movieapp.ui.adapter.PopularMovieAdapter
import com.byandev.movieapp.ui.repo.MoviePagedListRepository
import com.byandev.movieapp.ui.viewModel.HomeActivityViewModel
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeActivityViewModel

    lateinit var movieRepository: MoviePagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.elevation = 0f

        val apiService : ApiEndPoint = RetrofitClient.getClient()

        movieRepository = MoviePagedListRepository(apiService)

        viewModel = getViewModel()

        val movieAdapter = PopularMovieAdapter(this)

        val gridLayoutManager = GridLayoutManager(this, 2)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if (viewType == movieAdapter.MOVIE_VIEW_ITEM) return  1    // Movie_VIEW_TYPE will occupy 1 out of 3 span
                else return 3                                              // NETWORK_VIEW_TYPE will occupy all 3 span
            }
        };


        rv_movie_list.layoutManager = gridLayoutManager
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = movieAdapter

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_popular.visibility = if (viewModel.listItemEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility = if (viewModel.listItemEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listItemEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })

    }

    private fun getViewModel(): HomeActivityViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return HomeActivityViewModel(movieRepository) as T
            }
        })[HomeActivityViewModel::class.java]
    }
}
