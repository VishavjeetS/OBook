package com.example.obook.Model.AnimeModel.TopAnime


import com.google.gson.annotations.SerializedName

data class Anime(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("pagination")
    val pagination: Pagination
)