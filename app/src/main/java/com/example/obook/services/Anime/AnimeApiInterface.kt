package com.example.obook.services.Anime

import com.example.obook.Model.AnimeModel.AnimeFullDetail.FullDetail
import com.example.obook.Model.AnimeModel.Recommendation.AnimeRecommendation
import com.example.obook.Model.AnimeModel.TopAnime.Anime
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnimeApiInterface {
    @GET("top/anime")
    fun getTopAnime(@Query("page") page: Int) : Call<Anime>

    @GET("anime/{id}/recommendations")
    fun getAnimeRecommendations(@Path("id") page: Int) : Call<AnimeRecommendation>

    @GET("anime/{id}/full")
    fun getFullDetail(@Path("id") page: Int) : Call<FullDetail>
}