package com.example.obook.model.animeModel.anime.animeFullDetail


import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

@Entity(tableName = "anime")
data class Data(
    @SerializedName("aired")
    val aired: Aired,
    @SerializedName("airing")
    val airing: Boolean,
    @SerializedName("approved")
    val approved: Boolean,
    @SerializedName("background")
    val background: String? = "",
    @SerializedName("broadcast")
    val broadcast: Broadcast,
    @SerializedName("demographics")
    val demographics: List<Demographic>,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("episodes")
    val episodes: Int,
    @SerializedName("explicit_genres")
    val explicitGenres: List<ExplicitGenre>,
    @SerializedName("external")
    val `external`: List<External>,
    @SerializedName("favorites")
    val favorites: Int,
    @SerializedName("genres")
    val genres: List<Genre>,
    @SerializedName("images")
    val images: Images,
    @SerializedName("licensors")
    val licensors: List<Licensor>,
    @PrimaryKey
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("members")
    val members: Int,
    @SerializedName("popularity")
    val popularity: Int,
    @SerializedName("producers")
    val producers: List<Producer>,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("rating")
    val rating: String,
    @SerializedName("relations")
    val relations: List<Relation>,
    @SerializedName("score")
    val score: Double,
    @SerializedName("scored_by")
    val scoredBy: Int,
    @SerializedName("season")
    val season: String? = "",
    @SerializedName("source")
    val source: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("streaming")
    val streaming: List<Streaming>,
    @SerializedName("studios")
    val studios: List<Studio>,
    @SerializedName("synopsis")
    val synopsis: String,
    @SerializedName("theme")
    val theme: Theme,
    @SerializedName("themes")
    val themes: List<ThemeX>,
    @SerializedName("title")
    val title: String,
    @SerializedName("title_english")
    val titleEnglish: String? = "",
    @SerializedName("title_japanese")
    val titleJapanese: String,
    @SerializedName("title_synonyms")
    val titleSynonyms: List<String>,
    @SerializedName("titles")
    val titles: List<Title>,
    @SerializedName("trailer")
    val trailer: Trailer,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("year")
    val year: Int
)

class Convertors{
    @TypeConverter
    fun fromAired(aired: Aired?): String? {
        return aired?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toAired(airedString: String?): Aired? {
        return airedString?.let { Gson().fromJson(it, Aired::class.java) }
    }

    @TypeConverter
    fun fromBroadcast(broadcast: Broadcast?): String? {
        return broadcast?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toBroadcast(broadcastString: String?): Broadcast? {
        return broadcastString?.let { Gson().fromJson(it, Broadcast::class.java) }
    }

    @TypeConverter
    fun fromDemographics(demographics: List<Demographic>?): String? {
        return Gson().toJson(demographics)
    }

    @TypeConverter
    fun toDemographics(demographicsString: String?): List<Demographic>? {
        return Gson().fromJson(demographicsString, object : TypeToken<List<Demographic>>() {}.type)
    }

    @TypeConverter
    fun fromExplicitGenre(explicitGenre: List<ExplicitGenre?>): String? {
        return Gson().toJson(explicitGenre)
    }

    @TypeConverter
    fun toExplicitGenre(explicitGenreString: String?): List<ExplicitGenre?> {
        return Gson().fromJson(explicitGenreString, object : TypeToken<List<ExplicitGenre>>() {}.type)
    }

    @TypeConverter
    fun fromExternal(external: List<External?>): String? {
        return Gson().toJson(external)
    }

    @TypeConverter
    fun toExternal(externalString: String?): List<External?> {
        return Gson().fromJson(externalString, object : TypeToken<List<External>>() {}.type)
    }

    @TypeConverter
    fun fromGenre(genre: List<Genre?>): String? {
        return Gson().toJson(genre)
    }

    @TypeConverter
    fun toGenre(genreString: String?): List<Genre?> {
        return Gson().fromJson(genreString, object : TypeToken<List<Genre>>() {}.type)
    }

    @TypeConverter
    fun fromImages(images: Images?): String? {
        return images?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toImages(imagesString: String?): Images? {
        return imagesString?.let { Gson().fromJson(it, Images::class.java) }
    }

    @TypeConverter
    fun fromLicensor(licensor: List<Licensor?>): String? {
        return Gson().toJson(licensor)
    }

    @TypeConverter
    fun toLicensor(licensorString: String?): List<Licensor?> {
        return Gson().fromJson(licensorString, object : TypeToken<List<Licensor>>() {}.type)
    }

    @TypeConverter
    fun titleSynonyms(titleSynonyms: List<String?>): String? {
        return Gson().toJson(titleSynonyms)
    }

    @TypeConverter
    fun toTitleSynonyms(titleSynonymsString: String?): List<String?> {
        return Gson().fromJson(titleSynonymsString, object : TypeToken<List<String>>() {}.type)
    }

    @TypeConverter
    fun fromProducer(producer: List<Producer?>): String? {
        return Gson().toJson(producer)
    }

    @TypeConverter
    fun toProducer(producerString: String?): List<Producer?> {
        return Gson().fromJson(producerString, object : TypeToken<List<Producer>>() {}.type)
    }

    @TypeConverter
    fun fromRelation(relation: List<Relation?>): String? {
        return Gson().toJson(relation)
    }

    @TypeConverter
    fun toRelation(relationString: String?): List<Relation?> {
        return Gson().fromJson(relationString, object : TypeToken<List<Relation>>() {}.type)
    }

    @TypeConverter
    fun fromStreaming(streaming: List<Streaming?>): String? {
        return Gson().toJson(streaming)
    }

    @TypeConverter
    fun toStreaming(streamingString: String?): List<Streaming?> {
        return Gson().fromJson(streamingString, object : TypeToken<List<Streaming>>() {}.type)
    }

    @TypeConverter
    fun fromStudio(studio: List<Studio?>): String? {
        return Gson().toJson(studio)
    }

    @TypeConverter
    fun toStudio(studioString: String?): List<Studio?> {
        return Gson().fromJson(studioString, object : TypeToken<List<Studio>>() {}.type)
    }

    @TypeConverter
    fun fromTheme(theme: Theme?): String? {
        return theme?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toTheme(themeString: String?): Theme? {
        return themeString?.let { Gson().fromJson(it, Theme::class.java) }
    }

    @TypeConverter
    fun fromThemeX(themeX: List<ThemeX?>): String? {
        return Gson().toJson(themeX)
    }

    @TypeConverter
    fun toThemeX(themeXString: String?): List<ThemeX?> {
        return Gson().fromJson(themeXString, object : TypeToken<List<ThemeX>>() {}.type)
    }

    @TypeConverter
    fun fromTitle(title: List<Title?>): String? {
        return Gson().toJson(title)
    }

    @TypeConverter
    fun toTitle(titleString: String?): List<Title?> {
        return Gson().fromJson(titleString, object : TypeToken<List<Title>>() {}.type)
    }

    @TypeConverter
    fun fromTrailer(trailer: Trailer?): String? {
        return trailer?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toTrailer(trailerString: String?): Trailer? {
        return trailerString?.let { Gson().fromJson(it, Trailer::class.java) }
    }

}