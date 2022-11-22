package com.example.obook.Model.AnimeModel.AnimeFullDetail


import com.google.gson.annotations.SerializedName

data class To(
    @SerializedName("day")
    val day: Int,
    @SerializedName("month")
    val month: Int,
    @SerializedName("year")
    val year: Int
)