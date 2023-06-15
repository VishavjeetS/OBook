package com.example.obook.model.animeModel.anime.animeFullDetail


import com.google.gson.annotations.SerializedName

data class Prop(
    @SerializedName("from")
    val from: com.example.obook.model.animeModel.anime.animeFullDetail.From,
    @SerializedName("string")
    val string: String,
    @SerializedName("to")
    val to: To
)