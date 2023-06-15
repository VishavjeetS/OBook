package com.example.obook.model.animeModel.Manga.jinka


import com.google.gson.annotations.SerializedName

data class Published(
    @SerializedName("from")
    val from: String,
    @SerializedName("prop")
    val prop: Prop,
    @SerializedName("to")
    val to: String
)