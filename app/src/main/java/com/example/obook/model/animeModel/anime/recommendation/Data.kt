package com.example.obook.model.animeModel.anime.recommendation


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("entry")
    val entry: com.example.obook.model.animeModel.anime.recommendation.Entry
)