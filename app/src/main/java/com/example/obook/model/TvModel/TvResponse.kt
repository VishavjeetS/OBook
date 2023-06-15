package com.example.obook.model.TvModel

import android.os.Parcelable
import android.util.Log
import com.example.obook.model.CastModel.Cast
import com.example.obook.model.CastModel.CastResponse
import com.example.obook.model.KeywordModel.KeywordResponse
import com.example.obook.model.KeywordModel.Keywords
import com.example.obook.model.TvModel.Responses.TV
import com.example.obook.model.TvModel.tvSeasonResponse.Episode
import com.example.obook.model.VideoModel.VideoResponse
import com.example.obook.model.VideoModel.Videos
import com.example.obook.services.TVShows.TvApiInterface
import com.example.obook.services.TVShows.TvApiService
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Parcelize
class TvResponse(
    @SerializedName("results")
    val result: List<TV>,
    @SerializedName("total_pages")
    val tp: String,
) : Parcelable {
    constructor() : this(mutableListOf(), "")


    companion object {
        private val tvApiService: TvApiInterface = TvApiService.getInstance().create(TvApiInterface::class.java)

        fun getEpisodes(id: Int, sNum: Int, callback: (List<Episode>) -> Unit) {
            tvApiService.getTvSeason(id, sNum).enqueue(object : Callback<TvSeasonResponse> {
                override fun onResponse(
                    call: Call<TvSeasonResponse>,
                    response: Response<TvSeasonResponse>,
                ) {
                    if(response.isSuccessful) {
                        val season = response.body()!!
                        Log.d("site", season.episodes.toString())
                        return callback(season.episodes)
                    }
                    else{
                        Log.d("site", response.message())
                        Log.d("code", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<TvSeasonResponse>, t: Throwable) {
                    Log.d("site", t.message.toString())
                }

            })
        }

        fun getTvTrailers(id: Int, callback: (List<Videos>) -> Unit) {
            tvApiService.getTvTrailer(id).enqueue(object : Callback<VideoResponse> {
                override fun onResponse(
                    call: Call<VideoResponse>,
                    response: Response<VideoResponse>,
                ) {
                    val videos = response.body()!!.videos
                    return callback(videos)
                }

                override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                    Log.d("site", t.message.toString())
                }

            })
        }

        fun getTvCredits(id: Int, callback: (List<Cast>) -> Unit) {
            tvApiService.getTvCredits(id).enqueue(object : Callback<CastResponse> {
                override fun onResponse(
                    call: Call<CastResponse>,
                    response: Response<CastResponse>,
                ) {
                    val cast = response.body()!!.userList
                    return callback(cast)
                }

                override fun onFailure(call: Call<CastResponse>, t: Throwable) {
                    Log.d("site", t.message.toString())
                }

            })
        }

        fun getTvRecommendation(id: Int, page: Int, callback: (List<TV>) -> Unit) {
            tvApiService.getTvRecommendation(id, page).enqueue(object : Callback<TvResponse> {
                override fun onResponse(
                    call: Call<TvResponse>,
                    response: Response<TvResponse>,
                ) {
                    val tv = response.body()!!.result
                    return callback(tv)
                }

                override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                    Log.d("site", t.message.toString())
                }

            })
        }

        fun getTvSimilar(id: Int, page: Int, callback: (List<TV>) -> Unit) {
            tvApiService.getTvSimilar(id, page).enqueue(object : Callback<TvResponse> {
                override fun onResponse(
                    call: Call<TvResponse>,
                    response: Response<TvResponse>,
                ) {
                    val tv = response.body()!!.result
                    return callback(tv)
                }

                override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                    Log.d("site", t.message.toString())
                }

            })
        }

        fun getTvKeywords(id: Int, callback: (List<Keywords>) -> Unit) {
            tvApiService.getTvKeywords(id).enqueue(object : Callback<KeywordResponse> {
                override fun onResponse(
                    call: Call<KeywordResponse>,
                    response: Response<KeywordResponse>,
                ) {
                    val keywords = response.body()!!.keywords
                    return callback(keywords)
                }

                override fun onFailure(call: Call<KeywordResponse>, t: Throwable) {
                    Log.d("site", t.message.toString())
                }

            })
        }

        fun getDiscoverTv(page: Int, callback: (List<TV>) -> Unit) {
            tvApiService.getTvDiscover(page).enqueue(object : Callback<TvResponse> {
                override fun onResponse(
                    call: Call<TvResponse>,
                    response: Response<TvResponse>,
                ) {
                    val tv = response.body()!!.result
                    return callback(tv)
                }

                override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                    Log.d("site", t.message.toString())
                }

            })
        }

        fun getTvDetails(id: Int, callback: (TvDetailResponse) -> Unit) {
            tvApiService.getTvDetails(id).enqueue(object : Callback<TvDetailResponse> {
                override fun onResponse(
                    call: Call<TvDetailResponse>,
                    response: Response<TvDetailResponse>,
                ) {
                    val tv = response.body()!!
                    return callback(tv)
                }

                override fun onFailure(call: Call<TvDetailResponse>, t: Throwable) {
                    Log.d("site", t.message.toString())
                }

            })
        }

        fun getTvSearch(query: String, callback: (List<TV>) -> Unit) {
            tvApiService.getTvSearch(query).enqueue(object : Callback<TvResponse> {
                override fun onResponse(
                    call: Call<TvResponse>,
                    response: Response<TvResponse>,
                ) {
                    if(response.body() != null){
                        val tv = response.body()!!.result
                        return callback(tv)
                    }
                }

                override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                    Log.d("site", t.message.toString())
                }

            })
        }

    }

}