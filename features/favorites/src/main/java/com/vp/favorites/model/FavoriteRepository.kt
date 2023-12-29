package com.vp.favorites.model

import com.vp.daggeraddons.IoDispatcher
import com.vp.database.dao.FavoriteMoviesDao
import com.vp.database.entity.FavoriteMovieEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    private val favoriteMoviesDao: FavoriteMoviesDao,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {
    fun getFavoriteMovies(): Flow<List<FavoriteMovie>> = favoriteMoviesDao.selectAll()
        .map { favoriteMovies ->
            favoriteMovies.map { it.map() }
        }
        .flowOn(ioDispatcher)

    private fun FavoriteMovieEntity.map(): FavoriteMovie = FavoriteMovie(
        id = imdbId,
        poster = poster
    )
}
