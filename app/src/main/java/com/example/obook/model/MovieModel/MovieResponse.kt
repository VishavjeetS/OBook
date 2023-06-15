package com.example.obook.model.MovieModel

import android.os.Parcelable
import android.util.Log
import com.example.obook.model.CastModel.Cast
import com.example.obook.model.CastModel.CastResponse
import com.example.obook.model.KeywordModel.KeywordResponse
import com.example.obook.model.KeywordModel.Keywords
import com.example.obook.model.MovieModel.Responses.Movies
import com.example.obook.model.VideoModel.VideoResponse
import com.example.obook.model.VideoModel.Videos
import com.example.obook.services.Movie.MovieApiInterface
import com.example.obook.services.Movie.MovieApiService
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Parcelize
class MovieResponse(
    @SerializedName("results")
    val movies: List<Movies>,
    @SerializedName("total_pages")
    val tp: String
):Parcelable{
    constructor() : this(mutableListOf(), "")

    companion object{
        private val movieApiService: MovieApiInterface = MovieApiService.getInstance().create(MovieApiInterface::class.java)

        fun getGenre(callback: (List<Genre>) -> Unit){
            movieApiService.getGenres().enqueue(object : Callback<GenreResponse>{
                override fun onResponse(call: Call<GenreResponse>, response: Response<GenreResponse>) {
                    if (response.isSuccessful){
                        val genres = response.body()!!.genres
                        return callback(genres)
                    }
                    else{
                        Log.d("site", response.message())
                    }
                }

                override fun onFailure(call: Call<GenreResponse>, t: Throwable) {
                    Log.d("site", t.message.toString())
                }

            })
        }
        fun getMoviePopular(page: Int, callback: (MovieResponse) -> Unit){
            movieApiService.getMoviesPopular(page).enqueue(object : Callback<MovieResponse>{
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    if (response.isSuccessful){
                        val data = response.body()!!
                        return callback(data)
                    }
                    else{
                        Log.d("site", response.message())
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d("site", t.message.toString())
                }

            })
        }

//        fun getMoviePopular(page: Int, callback: (MovieNewResponse) -> Unit){
//            movieApiService.getMoviesPopular(page).enqueue(object : Callback<MovieNewResponse>{
//                override fun onResponse(call: Call<MovieNewResponse>, response: Response<MovieNewResponse>) {
//                    if (response.isSuccessful){
//                        val data = response.body()!!
//                        return callback(data)
//                    }
//                    else{
//                        Log.d("site", response.message())
//                    }
//                }
//
//                override fun onFailure(call: Call<MovieNewResponse>, t: Throwable) {
//                    Log.d("site", t.message.toString())
//                }
//
//            })
//        }

        fun getMovieTrailers(id: Int, callback: (List<Videos>) -> Unit){
            movieApiService.getMoviesTrailer(id).enqueue(object : Callback<VideoResponse>{
                override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                    val videos = response.body()!!.videos
                    return callback(videos)
                }

                override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                    Log.d("fail", t.message.toString())
                }

            })
        }

        fun getMovieCredits(id: Int, callback: (List<Cast>) -> Unit){
            movieApiService.getCredits(id).enqueue(object : Callback<CastResponse>{
                override fun onResponse(call: Call<CastResponse>, response: Response<CastResponse>) {
                    val cast = response.body()!!.userList
                    return callback(cast)
                }

                override fun onFailure(call: retrofit2.Call<CastResponse>, t: Throwable) {
                    Log.d("fail", t.message.toString())
                }

            })
        }

        fun getMovieKeywords(id: Int, callback: (List<Keywords>) -> Unit){
            movieApiService.getKeywords(id).enqueue(object : retrofit2.Callback<KeywordResponse>{
                override fun onResponse(call: Call<KeywordResponse>, response: Response<KeywordResponse>) {
                    val keywords = response.body()!!.keywords
                    return callback(keywords)
                }

                override fun onFailure(call: Call<KeywordResponse>, t: Throwable) {
                    Log.d("fail", t.message.toString())
                }

            })
        }

        fun getMovieRecommendations(id: Int, page: Int, callback: (List<Movies>) -> Unit){
            movieApiService.getRecommendation(id, page).enqueue(object : Callback<MovieResponse>{
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    val movies = response.body()!!.movies
                    return callback(movies)
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d("fail", t.message.toString())
                }

            })
        }

        fun getMovieSimilar(id: Int, page: Int, callback: (List<Movies>) -> Unit){
            movieApiService.getSimilar(id, page).enqueue(object : Callback<MovieResponse>{
                override fun onResponse(call: Call<MovieResponse>, response: retrofit2.Response<MovieResponse>) {
                    val movies = response.body()!!.movies
                    return callback(movies)
                }

                override fun onFailure(call: retrofit2.Call<MovieResponse>, t: Throwable) {
                    Log.d("fail", t.message.toString())
                }

            })
        }

        fun discoverMovies(page:Int, callback: (List<Movies>) -> Unit){
            movieApiService.discoverMovies(page).enqueue(object : Callback<MovieResponse>{
                override fun onResponse(call: retrofit2.Call<MovieResponse>, response: retrofit2.Response<MovieResponse>) {
                    val movies = response.body()!!.movies
                    return callback(movies)
                }

                override fun onFailure(call: retrofit2.Call<MovieResponse>, t: Throwable) {
                    Log.d("fail", t.message.toString())
                }

            })
        }

        fun getMovieDetail(id: Int, callback: (MovieDetailResponse) -> Unit){
            movieApiService.getMovieDetails(id).enqueue(object : Callback<MovieDetailResponse>{
                override fun onResponse(call: Call<MovieDetailResponse>, response: Response<MovieDetailResponse>) {
                    val movie = response.body()!!
                    return callback(movie)
                }

                override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                    Log.d("fail", t.message.toString())
                }

            })
        }

        fun getMovieSearch(query: String, page: Int, callback: (MovieResponse) -> Unit){
            movieApiService.searchMovie(query, page).enqueue(object : Callback<MovieResponse>{
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>,
                ) {
                    return callback(response.body()!!)
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d("fail", t.message.toString())
                }

            })
        }

    }
}