package com.ivanb.jobprepapp

import com.google.gson.annotations.SerializedName

// Dodana klasa SerachResponse jer JSON koji API vrati na vrhu nije lista — to je OBJEKT s poljem docs koje sadrži listu.
data class SearchResponse(
    val docs: List<OpenLibraryBookDto>
)

data class OpenLibraryBookDto(
    val title: String,
    @SerializedName("author_name") val authorName: List<String>?,
    @SerializedName("first_publish_year") val firstPublishYear: Int?,
    @SerializedName("cover_i") val coverId: Int?
)