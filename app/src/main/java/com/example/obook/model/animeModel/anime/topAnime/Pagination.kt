package com.example.obook.model.animeModel.anime.topAnime


import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("has_next_page")
    val hasNextPage: Boolean,
    @SerializedName("items")
    val items: com.example.obook.model.animeModel.anime.topAnime.Items,
    @SerializedName("last_visible_page")
    val lastVisiblePage: Int
)