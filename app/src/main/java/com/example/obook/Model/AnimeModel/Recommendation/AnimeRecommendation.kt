package com.example.obook.Model.AnimeModel.Recommendation


import com.google.gson.annotations.SerializedName

data class AnimeRecommendation(
    @SerializedName("data")
    val `data`: List<Data>
)