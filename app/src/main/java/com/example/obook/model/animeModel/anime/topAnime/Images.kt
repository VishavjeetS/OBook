package com.example.obook.model.animeModel.anime.topAnime


import com.google.gson.annotations.SerializedName

data class Images(
    @SerializedName("jpg")
    val jpg: com.example.obook.model.animeModel.anime.topAnime.Jpg,
    @SerializedName("webp")
    val webp: com.example.obook.model.animeModel.anime.topAnime.Webp
)