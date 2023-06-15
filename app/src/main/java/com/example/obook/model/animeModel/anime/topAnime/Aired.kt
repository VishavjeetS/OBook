package com.example.obook.model.animeModel.anime.topAnime


import com.google.gson.annotations.SerializedName

data class Aired(
    @SerializedName("from")
    val from: String,
    @SerializedName("prop")
    val prop: com.example.obook.model.animeModel.anime.topAnime.Prop,
    @SerializedName("to")
    val to: String
)