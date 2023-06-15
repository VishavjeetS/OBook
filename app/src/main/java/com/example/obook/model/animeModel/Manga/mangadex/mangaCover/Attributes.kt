package com.example.obook.model.animeModel.Manga.mangadex.mangaCover


import com.google.gson.annotations.SerializedName

data class Attributes(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("fileName")
    val fileName: String,
    @SerializedName("locale")
    val locale: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("version")
    val version: Int,
    @SerializedName("volume")
    val volume: String
)