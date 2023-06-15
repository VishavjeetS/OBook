package com.example.obook.services.anime

import com.example.obook.model.animeModel.anime.animeCharacters.CharacterResponse
import com.example.obook.model.animeModel.anime.animeEpisodes.Episodes
import com.example.obook.model.animeModel.anime.animeFullDetail.FullDetail
import com.example.obook.model.animeModel.Manga.jinka.Manga
import com.example.obook.model.animeModel.anime.recommendation.AnimeRecommendation
import com.example.obook.model.animeModel.anime.topAnime.Anime
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnimeApiInterface {
    @GET("top/anime")
    fun getTopAnime(@Query("page") page: Int) : Call<Anime>

    @GET("anime/{id}/recommendations")
    fun getAnimeRecommendations(@Path("id") id: Int, @Query("page") page: Int, ) : Call<AnimeRecommendation>

    @GET("anime/{id}/full")
    fun getFullDetail(@Path("id") id: Int) : Call<FullDetail>

    @GET("anime/{id}/characters")
    fun getCharacters(@Path("id") id: Int) : Call<CharacterResponse>

    @GET("anime/{id}/episodes")
    fun getEpisodes(@Path("id") id: Int) : Call<Episodes>

    @GET("manga")
    fun getManga(@Query("q") query: String): Call<Manga>

    @GET("anime")
    fun searchAnime(@Query("q") anime: String): Call<Anime>

//    @GET("manga/{manga_id}/chapters/{chapter_number}")
//    fun getChapter(@Path("manga_id") mangaId: Int, @Path("chapter_number") chapterNumber: Int, ): Call<Chapter?>?
}