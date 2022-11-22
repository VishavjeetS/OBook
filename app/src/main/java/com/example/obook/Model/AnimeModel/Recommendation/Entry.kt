package com.example.obook.Model.AnimeModel.Recommendation


import com.google.gson.annotations.SerializedName

data class Entry(
    @SerializedName("images")
    val images: Images,
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)