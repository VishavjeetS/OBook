package com.example.obook.model.animeModel.anime.recommendation


import com.google.gson.annotations.SerializedName

data class Entry(
    @SerializedName("images")
    val images: com.example.obook.model.animeModel.anime.recommendation.Images,
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)