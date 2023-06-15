package com.example.obook.model.animeModel.Manga.mangadex.response.mangaResponse

import android.util.Log
import com.example.obook.model.animeModel.Manga.jinka.Manga
import com.example.obook.model.animeModel.Manga.mangadex.chapterImages.ChapterImagesResponse
import com.example.obook.model.animeModel.Manga.mangadex.mangaChapters.MangaChaptersResponse
import com.example.obook.model.animeModel.Manga.mangadex.mangaCover.MangaCover
import com.example.obook.model.animeModel.Manga.mangadex.mangaList.Data
import com.example.obook.model.animeModel.Manga.mangadex.mangaList.MangaListResponse
import com.example.obook.services.anime.AnimeApiInterface
import com.example.obook.services.anime.AnimeApiService
import com.example.obook.services.manga.MangaApiInterface
import com.example.obook.services.manga.MangaApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MangaResponse {
    companion object{
        private val apiService = MangaApiService.getInstance().create(MangaApiInterface::class.java)

        private val imageService = AnimeApiService.getInstance().create(AnimeApiInterface::class.java)

        fun getMangaImage(query: String, callback: (List<com.example.obook.model.animeModel.Manga.jinka.Data>) -> Unit){
            imageService.getManga(query).enqueue(object: Callback<Manga?>{
                override fun onResponse(call: Call<Manga?>, response: Response<Manga?>) {
                    if(response.isSuccessful){
                        val manga = response.body()!!.data
                        return callback(manga)
                    }
                }

                override fun onFailure(call: Call<Manga?>, t: Throwable) {
                    Log.d("error", t.message.toString())
                }

            })
        }

        fun getMangaCover(mangaId: String, callback: (com.example.obook.model.animeModel.Manga.mangadex.mangaCover.Data) -> Unit){
            apiService.getCover(mangaId).enqueue(object: Callback<MangaCover>{
                override fun onResponse(call: Call<MangaCover>, response: Response<MangaCover>) {
                    if(response.isSuccessful){
                        return callback(response.body()!!.data)
                    }
                }

                override fun onFailure(call: Call<MangaCover>, t: Throwable) {
                    Log.d("error", t.message.toString())
                }

            })
        }

        fun getMangaList(limit: Int, callback: (List<Data>) -> Unit){
            apiService.getMangaList(limit).enqueue(object: Callback<MangaListResponse>{
                override fun onResponse(
                    call: Call<MangaListResponse>,
                    response: Response<MangaListResponse>,
                ) {
                    if(response.isSuccessful){
                        Log.d("total", response.body()!!.total.toString())
                        Log.d("limit", response.body()!!.limit.toString())
                        Log.d("offset", response.body()!!.offset.toString())
                        return callback(response.body()?.data!!)
                    }
                }

                override fun onFailure(call: Call<MangaListResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }

        fun getMangaChapters(mangaId: String, callback: (MangaChaptersResponse) -> Unit){
            apiService.getMangaChapters(mangaId).enqueue(object: Callback<MangaChaptersResponse>{
                override fun onResponse(
                    call: Call<MangaChaptersResponse>,
                    response: Response<MangaChaptersResponse>,
                ) {
                    if(response.isSuccessful) {
                        return callback(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<MangaChaptersResponse>, t: Throwable) {
                    Log.d("mangachaptererror", t.message.toString())
                }

            })
        }

        fun getMangaImages(chapterId: String, callback: (ChapterImagesResponse) -> Unit){
            apiService.getChapterImages(chapterId).enqueue(object: Callback<ChapterImagesResponse>{
                override fun onResponse(
                    call: Call<ChapterImagesResponse>,
                    response: Response<ChapterImagesResponse>,
                ) {
                    if(response.isSuccessful){
                        return callback(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<ChapterImagesResponse>, t: Throwable) {
                    Log.d("mangaimageerror", t.message.toString())
                }

            })
        }
    }
}