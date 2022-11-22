package com.example.obook.services.TVShows

import com.example.obook.Model.CastModel.CastResponse
import com.example.obook.Model.KeywordModel.KeywordResponse
import com.example.obook.Model.TvModel.TvResponse
import com.example.obook.Model.VideoModel.VideoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvApiInterface {
    @GET("tv/popular?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun getTvList(@Query("page") page: Int): Call<TvResponse>

    @GET("tv/top_rated?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun getTvTrendingList(@Query("page") page: Int): Call<TvResponse>

    @GET("search/tv?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun getTvSearch(@Query("query") show: String): Call<TvResponse>

    @GET("tv/{tv_id}/videos?api_key=8f7e8262951851e9cd40e68b53f7df38&append_to_response=videos")
    fun getTvTrailer(@Path("tv_id") int: Int): Call<VideoResponse>

    @GET("tv/{tv_id}/credits?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun getTvCredits(@Path("tv_id") int: Int): Call<CastResponse>

    @GET("tv/{tv_id}/keywords?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun getTvKeywords(@Path("tv_id") int: Int): Call<KeywordResponse>

    @GET("tv/{tv_id}/recommendations?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun getTvRecommendation(@Path("tv_id") int: Int): Call<TvResponse>

    @GET("tv/{tv_id}/similar?api_key=8f7e8262951851e9cd40e68b53f7df38")
    fun getTvSimilar(@Path("tv_id") int:Int, @Query("page") page:Int): Call<TvResponse>
}