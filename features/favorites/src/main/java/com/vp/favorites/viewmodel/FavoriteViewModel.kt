package com.vp.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.vp.favorites.model.FavoriteRepository
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    favoriteRepository: FavoriteRepository
): ViewModel() {
    val favoriteMovies = favoriteRepository.getFavoriteMovies().asLiveData(context = viewModelScope.coroutineContext)
}
