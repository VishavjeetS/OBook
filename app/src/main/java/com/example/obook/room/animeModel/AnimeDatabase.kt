package com.example.obook.room.animeModel

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.obook.model.animeModel.anime.animeFullDetail.Convertors
import com.example.obook.model.animeModel.anime.animeFullDetail.Data

@Database(entities = [Data::class], version = 4, exportSchema = false)
@TypeConverters(Convertors::class)
abstract class AnimeDatabase: RoomDatabase() {
    abstract fun animeDAO(): AnimeDAO
}