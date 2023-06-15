package com.example.obook.room.movieModel

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.obook.model.MovieModel.Responses.Movies


@Dao
interface MovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movieId: Movies)

    @Update
    fun updateMovie(movieId: Movies)

    @Delete
    fun deleteMovie(movieId: Movies)

    @Query("DELETE FROM movies")
    fun nukeTable()

    @Query("SELECT * FROM movies")
    fun getMovies(): LiveData<List<Movies>>
}