package com.example.obook.model.animeModel.anime.topAnime


import android.util.Log
import com.example.obook.services.anime.AnimeApiInterface
import com.example.obook.services.anime.AnimeApiService
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class Anime(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("pagination")
    val pagination: Pagination
){
    companion object{
        private val animeApiService = AnimeApiService.getInstance().create(AnimeApiInterface::class.java)
        fun getSearchAnime(query: String, callback: (List<Data>) -> Unit){
            animeApiService.searchAnime(query).enqueue(object: Callback<Anime>{
                override fun onResponse(call: Call<Anime>, response: Response<Anime>) {
                    if(response.isSuccessful){
                        val searchList = response.body()!!.data
                        return callback(searchList)
                    }
                    else{
                        Log.d("Error Search", response.message().toString())
                    }
                }

                override fun onFailure(call: Call<Anime>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}