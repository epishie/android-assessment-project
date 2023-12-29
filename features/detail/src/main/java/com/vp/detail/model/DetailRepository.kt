package com.vp.detail.model

import com.vp.daggeraddons.IoDispatcher
import com.vp.database.dao.FavoriteMoviesDao
import com.vp.database.entity.FavoriteMovieEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailRepository @Inject constructor(
    private val favoriteMoviesDao: FavoriteMoviesDao,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun saveFavoriteMovie(imdbId: String, poster: String?) = withContext(ioDispatcher) {
        favoriteMoviesDao.insert(FavoriteMovieEntity(imdbId, poster))
    }

    suspend fun removeFavoriteMovie(imdbId: String) = withContext(ioDispatcher) {
        favoriteMoviesDao.delete(FavoriteMovieEntity(imdbId))
    }

    fun getFavoriteMovieState(imdbId: String) = favoriteMoviesDao.selectById(imdbId)
        .map { it != null }
        .flowOn(ioDispatcher)
}
