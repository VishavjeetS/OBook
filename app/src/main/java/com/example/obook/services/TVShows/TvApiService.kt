package com.example.obook.services.TVShows

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TvApiService {
    companion object{
        private val BASE_URL = "https://api.themoviedb.org/3/"
        private var retrofit: Retrofit? = null

        fun getInstance() : Retrofit {
            if(retrofit == null){
                retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
                    GsonConverterFactory.create()).build()
            }
            return retrofit!!
        }
    }
}