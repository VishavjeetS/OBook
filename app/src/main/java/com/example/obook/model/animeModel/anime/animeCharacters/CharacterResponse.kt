package com.example.obook.model.animeModel.anime.animeCharacters


import android.util.Log
import com.example.obook.services.anime.AnimeApiInterface
import com.example.obook.services.anime.AnimeApiService
import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("data")
    val `data`: List<Data>
){
    companion object{
        private val animeApiService =
            AnimeApiService.getInstance().create(AnimeApiInterface::class.java)

        fun getAnimeCharacters(id: Int, callback: (List<Data>) -> Unit){
            try {
                animeApiService.getCharacters(id).enqueue(object : retrofit2.Callback<CharacterResponse>{
                    override fun onResponse(
                        call: retrofit2.Call<CharacterResponse>,
                        response: retrofit2.Response<CharacterResponse>,
                    ) {
                        if(response.isSuccessful){
                            val characters = response.body()!!.data
                            return callback(characters)
                        }
                        else{
                            Log.d("Error Char", response.message().toString())
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<CharacterResponse>, t: Throwable) {
                        Log.d("Error Char F.", t.message.toString())
                    }

                })
            }catch (e: Exception) {
                Log.d("Error Char", e.message.toString())
            }
        }
    }
}