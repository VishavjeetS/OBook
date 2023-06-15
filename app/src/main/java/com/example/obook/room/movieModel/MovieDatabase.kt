package com.example.obook.room.movieModel

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.obook.model.MovieModel.Responses.Movies

@Database(entities = [Movies::class], version = 7, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDAO(): MovieDAO
}