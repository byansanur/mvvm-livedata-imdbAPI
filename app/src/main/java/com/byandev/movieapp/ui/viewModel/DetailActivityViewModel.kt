package com.byandev.movieapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.byandev.movieapp.data.model.MovieDetails
import com.byandev.movieapp.data.source.NetworkState
import com.byandev.movieapp.ui.repo.MovieDetailRepository
import io.reactivex.disposables.CompositeDisposable

class DetailActivityViewModel (private val movieRespository : MovieDetailRepository, movieId: Int) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movieDetails : LiveData<MovieDetails> by lazy {
        movieRespository.fetchSigleMovie(compositeDisposable,movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieRespository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}