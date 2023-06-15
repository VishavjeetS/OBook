package com.example.obook.model.animeModel.anime.animeFullDetail


import com.google.gson.annotations.SerializedName

data class Aired(
    @SerializedName("from")
    val from: String,
    @SerializedName("prop")
    val prop: com.example.obook.model.animeModel.anime.animeFullDetail.Prop,
    @SerializedName("to")
    val to: String
)