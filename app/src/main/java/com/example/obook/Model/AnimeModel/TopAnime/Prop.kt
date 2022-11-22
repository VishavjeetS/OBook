package com.example.obook.Model.AnimeModel.TopAnime


import com.google.gson.annotations.SerializedName

data class Prop(
    @SerializedName("from")
    val from: From,
    @SerializedName("string")
    val string: String,
    @SerializedName("to")
    val to: To
)