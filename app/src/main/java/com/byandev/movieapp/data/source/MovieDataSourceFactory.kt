package com.byandev.movieapp.data.source

import androidx.lifecycle.MutableLiveData
import com.byandev.movieapp.data.api.ApiEndPoint
import com.byandev.movieapp.data.model.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory (private val apiService: ApiEndPoint, private val compositeDisposable: CompositeDisposable)
    :  androidx.paging.DataSource.Factory<Int, Movie>(){

    val movieLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): androidx.paging.DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable)

        movieLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}