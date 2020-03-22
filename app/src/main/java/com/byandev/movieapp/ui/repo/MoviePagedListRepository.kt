package com.byandev.movieapp.ui.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.byandev.movieapp.data.api.ApiEndPoint
import com.byandev.movieapp.data.api.POST_PER_PAGE
import com.byandev.movieapp.data.model.Movie
import com.byandev.movieapp.data.source.MovieDataSource
import com.byandev.movieapp.data.source.MovieDataSourceFactory
import com.byandev.movieapp.data.source.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository (private val apiService : ApiEndPoint) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesDataSourceFactory.movieLiveDataSource, MovieDataSource::networkState)
    }
}