package com.example.headsup


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CelebrityItem(
    @SerializedName("name")
    val name: String, // Ronaldo
    @SerializedName("pk")
    val pk: Int, // 1
    @SerializedName("taboo1")
    val taboo1: String, // Manchester United
    @SerializedName("taboo2")
    val taboo2: String, // Real Madrid
    @SerializedName("taboo3")
    val taboo3: String // Football
)