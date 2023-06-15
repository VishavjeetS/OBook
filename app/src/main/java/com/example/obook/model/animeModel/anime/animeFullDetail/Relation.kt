package com.example.obook.model.animeModel.anime.animeFullDetail


import com.google.gson.annotations.SerializedName

data class Relation(
    @SerializedName("entry")
    val entry: List<com.example.obook.model.animeModel.anime.animeFullDetail.Entry>,
    @SerializedName("relation")
    val relation: String
)