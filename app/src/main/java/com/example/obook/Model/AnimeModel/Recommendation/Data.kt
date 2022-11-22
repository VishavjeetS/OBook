package com.example.obook.Model.AnimeModel.Recommendation


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("entry")
    val entry: Entry
)