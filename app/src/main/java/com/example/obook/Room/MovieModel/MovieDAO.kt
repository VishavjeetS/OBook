package com.example.obook.Room.MovieModel

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.obook.Model.MovieModel.Movies


@Dao
interface MovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movies)

    @Update
    fun updateMovie(movie: Movies)

    @Delete
    fun deleteMovie(movie: Movies)

    @Query("DELETE FROM movies")
    fun nukeTable()

    @Query("SELECT * FROM movies")
    fun getMovies(): LiveData<List<Movies>>
}