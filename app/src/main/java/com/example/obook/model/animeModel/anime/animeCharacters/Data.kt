package com.example.obook.model.animeModel.anime.animeCharacters


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("character")
    val character: Character,
    @SerializedName("role")
    val role: String,
    @SerializedName("voice_actors")
    val voiceActors: List<VoiceActor>
)