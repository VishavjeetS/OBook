package com.example.obook.model.animeModel.anime.recommendation


import android.util.Log
import com.example.obook.services.anime.AnimeApiInterface
import com.example.obook.services.anime.AnimeApiService
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class AnimeRecommendation(
    @SerializedName("data")
    val `data`: List<Data>
){
    companion object{
        private val animeApiService =
            AnimeApiService.getInstance().create(AnimeApiInterface::class.java)

        fun getAnimeRecommended(page: Int, id: Int, callback: (List<Data>) -> Unit) {
            try{
                animeApiService.getAnimeRecommendations(id, page).enqueue(object :
                    Callback<AnimeRecommendation> {
                    override fun onResponse(
                        call: Call<AnimeRecommendation>,
                        response: Response<AnimeRecommendation>,
                    ) {
                        if (!response.isSuccessful) {
                            Log.d("Error Recc", response.message().toString())
                        }
                        else{
                            val recommendation = response.body()!!.data
                            return callback(recommendation)
                        }
                    }

                    override fun onFailure(call: Call<AnimeRecommendation>, t: Throwable) {
                        Log.d("Error Recc F.", t.message.toString())
                    }

                })
            }catch (e: Exception){
                Log.d("Error Recc", e.message.toString())
            }
        }
    }
}