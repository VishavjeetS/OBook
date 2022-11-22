package com.example.obook.Room.TvModel

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.obook.Model.TvModel.TV

@Dao
interface TvDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTvShow(tv: TV)

    @Update
    fun updateTvShow(tv: TV)

    @Delete
    fun deleteTvShow(tv: TV)

    @Query("DELETE FROM tv")
    fun nukeTable()

    @Query("SELECT * FROM tv")
    fun getTvShows(): LiveData<List<TV>>
}