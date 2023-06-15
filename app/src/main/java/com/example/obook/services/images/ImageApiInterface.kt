package com.example.obook.services.images

import com.example.obook.model.animeModel.images.ImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApiInterface {

    @GET("search/photos")
    fun getImages(@Query("query") query: String, @Query("client_id") client_id: String): retrofit2.Call<ImageResponse>

}