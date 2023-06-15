package com.example.obook.model.animeModel.Manga.mangadex


import com.google.gson.annotations.SerializedName

data class Attributes(
    @SerializedName("chapter")
    val chapter: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("externalUrl")
    val externalUrl: Any,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("publishAt")
    val publishAt: String,
    @SerializedName("readableAt")
    val readableAt: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("translatedLanguage")
    val translatedLanguage: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("version")
    val version: Int,
    @SerializedName("volume")
    val volume: String
)