package com.example.obook.Model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Anime(
    @SerializedName("images")
    val images: List<Images>,

    @SerializedName("url")
    var url: String,

    @SerializedName("title")
    var title: String
):Parcelable

@Parcelize
class Images (
    @SerializedName("jpg")
    var jpg: List<Jpg?>? = null
): Parcelable

@Parcelize
class Jpg(
    @SerializedName("image_url")
    var jpg_img: String
): Parcelable

