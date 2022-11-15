package com.example.obook.services

import com.example.obook.Model.AnimeResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface AnimeApiInterface {
    @GET("random/anime")
    fun getRandomAnime() : Call<AnimeResponse>
}