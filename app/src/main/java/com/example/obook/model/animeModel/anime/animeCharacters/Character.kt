package com.example.obook.model.animeModel.anime.animeCharacters


import com.google.gson.annotations.SerializedName

data class Character(
    @SerializedName("images")
    val images: Images,
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)