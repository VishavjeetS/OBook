package com.example.obook.services.manga

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MangaApiService {
    companion object{
        private const val baseUrl = "https://api.mangadex.org/"
        private var retrofit: Retrofit? = null
        fun getInstance(): Retrofit {
            if(retrofit==null){
                retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl)
                    .build()
            }
            return retrofit!!
        }
    }
}