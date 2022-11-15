package com.example.obook.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AnimeApiService {
    companion object{
        private val BASE_URL = "https://api.jikan.moe/v4/"
        private var retrofit: Retrofit? = null

        fun getInstance() : Retrofit {
            if(retrofit == null){
                retrofit = Retrofit.Builder()
                    .baseUrl("https://api.jikan.moe/v4/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
    }
}