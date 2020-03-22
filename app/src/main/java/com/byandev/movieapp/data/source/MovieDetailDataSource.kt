package com.byandev.movieapp.data.source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.byandev.movieapp.data.api.ApiEndPoint
import com.byandev.movieapp.data.model.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MovieDetailDataSource (private val apiService: ApiEndPoint, private val compositeDisposable: CompositeDisposable) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState> get() = _networkState

    private val _downloadMovieDetailResponse = MutableLiveData<MovieDetails>()
    val downloadMovieDetailResponse: LiveData<MovieDetails> get() = _downloadMovieDetailResponse

    fun fetchMovieDetail(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMovieDetail(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadMovieDetailResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.d("movieDetail", it.message)
                        }
                    )
            )
        } catch (e: Exception) {
            Log.d("tryCatch movie detail", e.message)
        }
    }

}