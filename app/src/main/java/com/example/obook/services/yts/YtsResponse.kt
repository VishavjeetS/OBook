package com.example.obook.services.yts

import android.util.Log
import com.example.obook.model.MovieModel.ytsMovieModel.YtsMovieResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YtsResponse {
    companion object{
        private val ytsApiService = YtsApiService.getInstance().create(YtsApiInterface::class.java)

        fun getMovieDetail(imdb_id: String, callback: (YtsMovieResponse) -> Unit) {
            ytsApiService.getMovieDetails(imdb_id).enqueue(object : Callback<YtsMovieResponse>{
                override fun onResponse(call: Call<YtsMovieResponse>, response: Response<YtsMovieResponse>) {
                    if (response.isSuccessful){
                        Log.d("YtsResponse - Success", "success")
                        val data = response.body()!!
                        return callback(data)
                    }
                    else{
                        Log.d("YtsResponse - Fail", "getMovieDetail: ${response.errorBody()} ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<YtsMovieResponse>, t: Throwable) {
                    Log.d("YtsResponse - Failure", "getMovieDetail: ${t.message} ${call.request().url()}")
                }

            })
        }
    }
}