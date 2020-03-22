package com.byandev.movieapp.ui.repo

import androidx.lifecycle.LiveData
import com.byandev.movieapp.data.api.ApiEndPoint
import com.byandev.movieapp.data.model.MovieDetails
import com.byandev.movieapp.data.source.MovieDetailDataSource
import com.byandev.movieapp.data.source.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailRepository (private val apiService : ApiEndPoint) {
    lateinit var movieDetailDataSource: MovieDetailDataSource

    fun fetchSigleMovie(compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {
        movieDetailDataSource = MovieDetailDataSource(apiService,compositeDisposable)
        movieDetailDataSource.fetchMovieDetail(movieId)

        return movieDetailDataSource.downloadMovieDetailResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailDataSource.networkState
    }
}