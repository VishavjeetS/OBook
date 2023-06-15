package com.example.obook.services.manga

import com.example.obook.model.animeModel.Manga.mangadex.ChapterResponse
import com.example.obook.model.animeModel.Manga.mangadex.chapterImages.ChapterImagesResponse
import com.example.obook.model.animeModel.Manga.mangadex.mangaChapters.MangaChaptersResponse
import com.example.obook.model.animeModel.Manga.mangadex.mangaCover.MangaCover
import com.example.obook.model.animeModel.Manga.mangadex.mangaList.MangaListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MangaApiInterface {

    @GET("manga")
    fun getMangaList(@Query("limit") limit: Int): Call<MangaListResponse>

    @GET("chapter/{chapter_id}")
    fun getChapter(@Path("chapter_id") chapterId: String): Call<ChapterResponse?>?

    @GET("at-home/server/{chapter_id}")
    fun getChapterImages(@Path("chapter_id") chapterId: String): Call<ChapterImagesResponse>

    @GET("manga/{id}/aggregate")
    fun getMangaChapters(@Path("id") id: String): Call<MangaChaptersResponse>

    @GET("cover/{mangaOrCoverId}")
    fun getCover(@Path("mangaOrCoverId") mangaOrCoverId: String): Call<MangaCover>


}