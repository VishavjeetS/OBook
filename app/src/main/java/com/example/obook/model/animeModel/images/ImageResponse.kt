package com.example.obook.model.animeModel.images


import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)