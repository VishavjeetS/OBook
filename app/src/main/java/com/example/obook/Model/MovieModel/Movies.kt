package com.example.obook.Model.MovieModel

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull

@Parcelize
@Entity(tableName = "movies")
class Movies(
    @PrimaryKey
    @SerializedName("id")
    val id : String,
    @SerializedName("title")
    val title: String?,
    @SerializedName("vote_count")
    val vote_count:String?,
    @SerializedName("overview")
    val overview : String?,
    @SerializedName("poster_path")
    val poster_path:String?,
    @SerializedName("release_date")
    val release_date:String,
):Parcelable{
    constructor() : this("", "", "","","", "")
}