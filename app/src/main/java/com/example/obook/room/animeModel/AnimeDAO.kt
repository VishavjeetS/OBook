package com.example.obook.room.animeModel

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.obook.model.animeModel.anime.animeFullDetail.Data

@Dao
interface AnimeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnime(anime: Data)

    @Update
    fun updateAnime(anime: Data)

    @Delete
    fun deleteAnime(anime: Data)

    @Query("DELETE FROM anime")
    fun nukeTable()

    @Query("SELECT * FROM anime")
    fun getAnime(): LiveData<List<Data>>
}