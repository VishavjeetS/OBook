package com.example.obook.model.animeModel.anime.animeFullDetail


import android.util.Log
import com.example.obook.services.anime.AnimeApiInterface
import com.example.obook.services.anime.AnimeApiService
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.DelicateCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class FullDetail(
    @SerializedName("data")
    val `data`: Data
){

    companion object {
        private val animeApiService =
            AnimeApiService.getInstance().create(AnimeApiInterface::class.java)

        fun getAnimeData(id: Int, callback: (FullDetail) -> Unit){
            try {
                animeApiService.getFullDetail(id).enqueue(object : Callback<FullDetail> {
                    override fun onResponse(
                        call: Call<FullDetail>,
                        response: Response<FullDetail>,
                    ) {
                        if(!response.isSuccessful){
                            Log.d("Error - Full Detail", "${ response.message().toString()} - ${response.code().toString()}")
                        }
                        else{
                            val anime = response.body()!!
                            return callback(anime)
                        }
                    }

                    override fun onFailure(call: Call<FullDetail>, t: Throwable) {
                        Log.d("Error - Full Detail F.", t.message.toString())
                    }

                })
            }
            catch (e: Exception){
                Log.d("Error - Full Detail", e.message.toString())
            }
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun getAnimeDetails(id: Int, callback: (Data) -> Unit) {
            try {
                animeApiService.getFullDetail(id).enqueue(object : Callback<FullDetail> {
                    override fun onResponse(
                        call: Call<FullDetail>,
                        response: Response<FullDetail>,
                    ) {
                        if(!response.isSuccessful){
                            Log.d("Error - Full Detail", "${ response.message().toString()} - ${response.code().toString()}")
                        }
                        else{
                            val anime = response.body()!!.data
                            return callback(anime)
                        }
                    }

                    override fun onFailure(call: Call<FullDetail>, t: Throwable) {
                        Log.d("Error - Full Detail F.", t.message.toString())
                    }

                })
            }
            catch (e: Exception){
                Log.d("Error - Full Detail", e.message.toString())
            }
        }

    }
}