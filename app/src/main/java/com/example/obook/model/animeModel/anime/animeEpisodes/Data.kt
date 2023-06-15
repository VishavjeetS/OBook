package com.example.obook.model.animeModel.anime.animeEpisodes


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("aired")
    val aired: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("filler")
    val filler: Boolean,
    @SerializedName("forum_url")
    val forumUrl: String,
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("recap")
    val recap: Boolean,
    @SerializedName("title")
    val title: String,
    @SerializedName("title_japanese")
    val titleJapanese: String,
    @SerializedName("title_romanji")
    val titleRomanji: String,
    @SerializedName("url")
    val url: String
)