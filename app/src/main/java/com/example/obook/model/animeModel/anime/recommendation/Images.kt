package com.example.obook.model.animeModel.anime.recommendation


import com.google.gson.annotations.SerializedName

data class Images(
    @SerializedName("jpg")
    val jpg: com.example.obook.model.animeModel.anime.recommendation.Jpg,
    @SerializedName("webp")
    val webp: com.example.obook.model.animeModel.anime.recommendation.Webp
)