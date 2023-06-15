package com.example.obook.model.MovieModel.ytsMovieModel


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("api_version")
    val apiVersion: Int,
    @SerializedName("execution_time")
    val executionTime: String,
    @SerializedName("server_time")
    val serverTime: Int,
    @SerializedName("server_timezone")
    val serverTimezone: String
)