package com.example.obook.model.animeModel.anime.topAnime


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("aired")
    val aired: com.example.obook.model.animeModel.anime.topAnime.Aired,
    @SerializedName("airing")
    val airing: Boolean,
    @SerializedName("approved")
    val approved: Boolean,
    @SerializedName("background")
    val background: String,
    @SerializedName("broadcast")
    val broadcast: com.example.obook.model.animeModel.anime.topAnime.Broadcast,
    @SerializedName("demographics")
    val demographics: List<com.example.obook.model.animeModel.anime.topAnime.Demographic>,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("episodes")
    val episodes: Int,
    @SerializedName("explicit_genres")
    val explicitGenres: List<com.example.obook.model.animeModel.anime.topAnime.ExplicitGenre>,
    @SerializedName("favorites")
    val favorites: Int,
    @SerializedName("genres")
    val genres: List<com.example.obook.model.animeModel.anime.topAnime.Genre>,
    @SerializedName("images")
    val images: com.example.obook.model.animeModel.anime.topAnime.Images,
    @SerializedName("licensors")
    val licensors: List<com.example.obook.model.animeModel.anime.topAnime.Licensor>,
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("members")
    val members: Int,
    @SerializedName("popularity")
    val popularity: Int,
    @SerializedName("producers")
    val producers: List<com.example.obook.model.animeModel.anime.topAnime.Producer>,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("rating")
    val rating: String,
    @SerializedName("score")
    val score: Double,
    @SerializedName("scored_by")
    val scoredBy: Int,
    @SerializedName("season")
    val season: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("studios")
    val studios: List<com.example.obook.model.animeModel.anime.topAnime.Studio>,
    @SerializedName("synopsis")
    val synopsis: String,
    @SerializedName("themes")
    val themes: List<com.example.obook.model.animeModel.anime.topAnime.Theme>,
    @SerializedName("title")
    val title: String,
    @SerializedName("title_english")
    val titleEnglish: String,
    @SerializedName("title_japanese")
    val titleJapanese: String,
    @SerializedName("title_synonyms")
    val titleSynonyms: List<String>,
    @SerializedName("titles")
    val titles: List<com.example.obook.model.animeModel.anime.topAnime.Title>,
    @SerializedName("trailer")
    val trailer: com.example.obook.model.animeModel.anime.topAnime.Trailer,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("year")
    val year: Int
) {
    fun getData(): Data {
        return Data(
            aired,
            airing,
            approved,
            background,
            broadcast,
            demographics,
            duration,
            episodes,
            explicitGenres,
            favorites,
            genres,
            images,
            licensors,
            malId,
            members,
            popularity,
            producers,
            rank,
            rating,
            score,
            scoredBy,
            season,
            source,
            status,
            studios,
            synopsis,
            themes,
            title,
            titleEnglish,
            titleJapanese,
            titleSynonyms,
            titles,
            trailer,
            type,
            url,
            year
        )
    }
}