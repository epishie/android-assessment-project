package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vp.detail.DetailActivity
import com.vp.detail.model.DetailRepository
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(
    private val detailService: DetailService,
    private val detailRepository: DetailRepository
) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    private val isFavorite: MutableLiveData<Boolean> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun isFavorite(): LiveData<Boolean> = isFavorite

    fun fetchDetails() {
        loadingState.value = LoadingState.IN_PROGRESS
        val imdbId = DetailActivity.queryProvider.getMovieId()
        detailService.getMovie(imdbId).enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>?, response: Response<MovieDetail>?) {
                details.postValue(response?.body())

                response?.body()?.title?.let {
                    title.postValue(it)
                }

                loadingState.value = LoadingState.LOADED
            }

            override fun onFailure(call: Call<MovieDetail>?, t: Throwable?) {
                details.postValue(null)
                loadingState.value = LoadingState.ERROR
            }
        })
        viewModelScope.launch(Dispatchers.Main.immediate) {
            detailRepository.getFavoriteMovieState(imdbId)
                .collect {
                    isFavorite.value = it
                }
        }
    }

    fun setFavorite() {
        val imdbId = DetailActivity.queryProvider.getMovieId()
        val details = details.value ?: return
        viewModelScope.launch {
            detailRepository.saveFavoriteMovie(imdbId, details.poster)
        }
    }

    fun unsetFavorite() {
        val imdbId = DetailActivity.queryProvider.getMovieId()
        viewModelScope.launch {
            detailRepository.removeFavoriteMovie(imdbId)
        }
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}
