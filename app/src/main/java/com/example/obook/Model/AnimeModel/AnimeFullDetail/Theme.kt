package com.example.obook.Model.AnimeModel.AnimeFullDetail


import com.google.gson.annotations.SerializedName

data class Theme(
    @SerializedName("endings")
    val endings: List<String>,
    @SerializedName("openings")
    val openings: List<String>
)