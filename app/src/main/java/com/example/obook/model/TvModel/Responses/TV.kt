package com.example.obook.model.TvModel.Responses

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "tv")
class TV (
    @PrimaryKey
    @SerializedName("id")
    val id : String,
    @SerializedName("name")
    val title: String?,
    @SerializedName("vote_count")
    val vote_count:String?,
    @SerializedName("overview")
    val overview : String?,
    @SerializedName("poster_path")
    val poster_path:String?,
    @SerializedName("first_air_date")
    val release_date:String,
    @SerializedName("vote_average")
    val vote_average:String,
        ): Parcelable{
            constructor(): this("", "", "", "", "","", "")


        }