package com.example.obook.model.MovieModel.ytsMovieModel


import com.google.gson.annotations.SerializedName

data class YtsMovieResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("@meta")
    val meta: Meta,
    @SerializedName("status")
    val status: String,
    @SerializedName("status_message")
    val statusMessage: String
)