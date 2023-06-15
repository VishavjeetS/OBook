package com.example.obook.services.images

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ImageApiService {
    companion object{
        private const val baseUrl = "https://api.unsplash.com/"
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