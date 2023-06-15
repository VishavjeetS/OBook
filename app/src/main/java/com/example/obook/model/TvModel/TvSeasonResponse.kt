package com.example.obook.model.TvModel


import com.example.obook.model.TvModel.tvSeasonResponse.Episode
import com.google.gson.annotations.SerializedName

data class TvSeasonResponse(
    @SerializedName("air_date")
    val airDate: String,
    @SerializedName("episodes")
    val episodes: List<Episode>,
    @SerializedName("_id")
    val _id: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("season_number")
    val seasonNumber: Int
)