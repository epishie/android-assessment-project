package com.vp.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.list.model.ListItem
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val searchService: SearchService
) : ViewModel() {
    private val liveData = MutableLiveData<SearchResult>()
    private var currentTitle: String = ""
    private val aggregatedItems = mutableListOf<ListItem>()

    fun observeMovies(): LiveData<SearchResult> = liveData

    fun searchMoviesByTitle(title: String, page: Int) {
        if (page == 1 && title != currentTitle) {
            aggregatedItems.clear()
            currentTitle = title
            liveData.value = SearchResult.inProgress()
        }
        searchService.search(title, page).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>,
            ) {
                val result = response.body()
                if (result != null) {
                    aggregatedItems.addAll(result.search)
                    liveData.value = SearchResult.success(aggregatedItems, result.totalResults)
                } else {
                    liveData.value = SearchResult.success(aggregatedItems, 0)
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                liveData.value = SearchResult.error()
            }

        })
    }
}
