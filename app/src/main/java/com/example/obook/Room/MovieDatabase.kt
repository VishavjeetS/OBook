package com.example.obook.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.obook.Model.MovieModel.Movies

@Database(entities = [Movies::class], version = 7, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDAO(): MovieDAO
}