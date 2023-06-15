package com.example.obook.services.anime

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AnimeApiService {
    companion object{
        private const val BASE_URL = "https://api.jikan.moe/v4/"
        private var retrofit: Retrofit? = null
        private var client:  OkHttpClient? = null
        private const val DEFAULT_WAIT_TIME = 2000L

        fun getInstance() : Retrofit {
            if(retrofit == null || client == null){
                client = OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        var response = chain.proceed(chain.request())
                        var tryCount = 0
                        while (response.code() == 429 && tryCount < 3) {
                            // Wait for a certain amount of time before retrying the request
                            val waitTime = calculateWaitTime(response)
                            Thread.sleep(waitTime)

                            // Retry the request
                            tryCount++
                            response.close()
                            response = chain.proceed(chain.request())
                        }
                        response
                    }
                    .build()
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .client(client!!)
                    .build()
            }
            return retrofit!!
        }

        private fun calculateWaitTime(response: Response): Long {
            val retryAfterHeader = response.header("Retry-After")
            return if (retryAfterHeader != null) {
                retryAfterHeader.toLong() * 1000L // Convert seconds to milliseconds
            } else {
                // If no Retry-After header is present, wait for a default amount of time
                DEFAULT_WAIT_TIME
            }
        }
    }
}
