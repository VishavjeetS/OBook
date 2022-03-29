package com.example.obook.services

import com.example.obook.Model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET

interface MovieApiInterface {
    @GET("movie/popular?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun getMoviesList(): Call<MovieResponse>

    @GET("movie/top_rated?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun getTopRatedList(): Call<MovieResponse>
}