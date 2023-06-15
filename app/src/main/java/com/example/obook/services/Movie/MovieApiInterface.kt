package com.example.obook.services.Movie

import com.example.obook.model.CastModel.CastResponse
import com.example.obook.model.KeywordModel.KeywordResponse
import com.example.obook.model.MovieModel.GenreResponse
import com.example.obook.model.MovieModel.MovieDetailResponse
import com.example.obook.model.MovieModel.MovieResponse
import com.example.obook.model.VideoModel.VideoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiInterface {
    @GET("movie/{movie_id}?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun getMovieDetails(@Path("movie_id") int: Int): Call<MovieDetailResponse>

    @GET("movie/popular?api_key=8f7e8262951851e9cd40e68b53f7df38")
//    fun getMoviesPopular(@Query("page") page: Int): Call<MovieNewResponse>
    fun getMoviesPopular(@Query("page") page: Int): Call<MovieResponse>

    @GET("movie/top_rated?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun getTopRatedList(@Query("page") page: Int): Call<MovieResponse>

    @GET("movie/{movie_id}/videos?api_key=8f7e8262951851e9cd40e68b53f7df38&append_to_response=videos")
    fun getMoviesTrailer(@Path("movie_id") int: Int): Call<VideoResponse>

    @GET("movie/{movie_id}/recommendations?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun getRecommendation(@Path("movie_id") int: Int, @Query("page") page: Int): Call<MovieResponse>

    @GET("search/movie?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun searchMovie(@Query("query") movie: String, @Query("page") page: Int): Call<MovieResponse>

    @GET("movie/{movie_id}/similar?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun getSimilar(@Path("movie_id") int:Int, @Query("page") page: Int): Call<MovieResponse>

    @GET("movie/{movie_id}/credits?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun getCredits(@Path("movie_id") int: Int): Call<CastResponse>

    @GET("movie/{movie_id}/keywords?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun getKeywords(@Path("movie_id") int: Int): Call<KeywordResponse>

    @GET("discover/movie?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun discoverMovies(@Query("page") page: Int): Call<MovieResponse>

    @GET("genre/movie/list?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun getGenres(): Call<GenreResponse>
}