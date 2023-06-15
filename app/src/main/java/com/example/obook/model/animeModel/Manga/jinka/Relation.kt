package com.example.obook.model.animeModel.Manga.jinka


import com.google.gson.annotations.SerializedName

data class Relation(
    @SerializedName("entry")
    val entry: List<Entry>,
    @SerializedName("relation")
    val relation: String
)