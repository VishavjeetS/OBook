package com.example.obook.model.animeModel.anime.animeCharacters


import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("images")
    val images: ImagesX,
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)