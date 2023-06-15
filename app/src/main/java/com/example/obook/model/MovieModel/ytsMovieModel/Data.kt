package com.example.obook.model.MovieModel.ytsMovieModel


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("movie")
    val movie: Movie
)