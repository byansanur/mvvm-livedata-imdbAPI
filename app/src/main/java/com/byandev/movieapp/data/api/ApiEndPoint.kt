package com.byandev.movieapp.data.api

import com.byandev.movieapp.data.model.MovieDetails
import com.byandev.movieapp.data.model.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEndPoint {

    @GET("movie/popular")
    fun getMoviePopular(@Query("page") page: Int) : Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id") id: Int): Single<MovieDetails>
}