package com.example.obook.services

import com.example.obook.Model.MovieResponse
import com.example.obook.Model.VideoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApiInterface {
    @GET("movie/popular?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun getMoviesList(): Call<MovieResponse>

    @GET("movie/top_rated?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun getTopRatedList(): Call<MovieResponse>

    @GET("movie/{movie_id}/videos?api_key=8f7e8262951851e9cd40e68b53f7df38&append_to_response=videos")
    fun getMoviesTrailer(@Path("movie_id") int: Int): Call<VideoResponse>
}