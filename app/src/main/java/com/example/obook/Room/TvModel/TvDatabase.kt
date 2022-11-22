package com.example.obook.Room.TvModel

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.obook.Model.TvModel.TV

@Database(entities = [TV::class], version = 1, exportSchema = false)
abstract class TvDatabase: RoomDatabase() {
    abstract fun tvDAO(): TvDAO
}