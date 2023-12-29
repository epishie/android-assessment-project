package com.vp.list.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("Search")
    val _search: List<ListItem>?,
    @SerializedName("totalResults")
    val totalResults: Int = 0,
    @SerializedName("Response")
    val response: String
) {
    val search: List<ListItem> get() = _search ?: emptyList()
    val hasResponse: Boolean
        get() = POSITIVE_RESPONSE == response

    companion object {
        private const val POSITIVE_RESPONSE = "True"
    }
}

