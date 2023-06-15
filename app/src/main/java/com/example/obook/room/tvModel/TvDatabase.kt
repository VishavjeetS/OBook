package com.example.obook.room.tvModel

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.obook.model.TvModel.Responses.TV

@Database(entities = [TV::class], version = 1, exportSchema = false)
abstract class TvDatabase: RoomDatabase() {
    abstract fun tvDAO(): TvDAO
}