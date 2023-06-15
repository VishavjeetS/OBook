package com.example.obook.model.animeModel.Manga.jinka


import com.google.gson.annotations.SerializedName

data class Manga(
    @SerializedName("data")
    val `data`: List<Data>
)