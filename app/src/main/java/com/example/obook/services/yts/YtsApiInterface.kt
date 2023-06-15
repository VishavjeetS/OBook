package com.example.obook.services.yts

import com.example.obook.model.MovieModel.ytsMovieModel.YtsMovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YtsApiInterface {
    @GET("list_movies.json")
    fun getMovies(): Call<YtsMovieResponse>

    @GET("movie_suggestions.json")
    fun getMovieSuggestions()

    @GET("movie_details.json")
    fun getMovieDetails(@Query("imdb_id") id: String): Call<YtsMovieResponse>
}