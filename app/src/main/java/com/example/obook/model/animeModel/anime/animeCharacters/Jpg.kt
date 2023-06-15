package com.example.obook.model.animeModel.anime.animeCharacters


import com.google.gson.annotations.SerializedName

data class Jpg(
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("small_image_url")
    val smallImageUrl: String
)