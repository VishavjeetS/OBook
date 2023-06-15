package com.example.obook.model.animeModel.anime.topAnime


import com.google.gson.annotations.SerializedName

data class Prop(
    @SerializedName("from")
    val from: com.example.obook.model.animeModel.anime.topAnime.From,
    @SerializedName("string")
    val string: String,
    @SerializedName("to")
    val to: com.example.obook.model.animeModel.anime.topAnime.To
)