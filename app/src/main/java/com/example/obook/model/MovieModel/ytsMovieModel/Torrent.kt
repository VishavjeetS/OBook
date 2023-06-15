package com.example.obook.model.MovieModel.ytsMovieModel


import com.google.gson.annotations.SerializedName

data class Torrent(
    @SerializedName("date_uploaded")
    val dateUploaded: String,
    @SerializedName("date_uploaded_unix")
    val dateUploadedUnix: Int,
    @SerializedName("hash")
    val hash: String,
    @SerializedName("peers")
    val peers: Int,
    @SerializedName("quality")
    val quality: String,
    @SerializedName("seeds")
    val seeds: Int,
    @SerializedName("size")
    val size: String,
    @SerializedName("size_bytes")
    val sizeBytes: Long,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String
)