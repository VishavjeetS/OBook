package com.example.obook.Model.AnimeModel.AnimeFullDetail


import com.google.gson.annotations.SerializedName

data class Relation(
    @SerializedName("entry")
    val entry: List<Entry>,
    @SerializedName("relation")
    val relation: String
)