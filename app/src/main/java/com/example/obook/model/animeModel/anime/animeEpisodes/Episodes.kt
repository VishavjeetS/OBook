package com.example.obook.model.animeModel.anime.animeEpisodes


import com.google.gson.annotations.SerializedName

data class Episodes(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("pagination")
    val pagination: Pagination
)