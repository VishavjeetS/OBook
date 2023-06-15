package com.example.obook.model.animeModel.Manga.mangadex.mangaList


import com.google.gson.annotations.SerializedName

data class Attributes(
    @SerializedName("altTitles")
    val altTitles: List<AltTitle>,
    @SerializedName("availableTranslatedLanguages")
    val availableTranslatedLanguages: List<Any>,
    @SerializedName("chapterNumbersResetOnNewVolume")
    val chapterNumbersResetOnNewVolume: Boolean,
    @SerializedName("contentRating")
    val contentRating: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("description")
    val description: Description,
    @SerializedName("isLocked")
    val isLocked: Boolean,
    @SerializedName("lastChapter")
    val lastChapter: String,
    @SerializedName("lastVolume")
    val lastVolume: String,
    @SerializedName("latestUploadedChapter")
    val latestUploadedChapter: String,
    @SerializedName("links")
    val links: Links,
    @SerializedName("originalLanguage")
    val originalLanguage: String,
    @SerializedName("publicationDemographic")
    val publicationDemographic: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("tags")
    val tags: List<Tag>,
    @SerializedName("title")
    val title: Title,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("version")
    val version: Int,
    @SerializedName("year")
    val year: Int
)