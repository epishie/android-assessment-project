package com.vp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vp.database.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMoviesDao {
    @Insert
    suspend fun insert(favoriteMovie: FavoriteMovieEntity)

    @Delete
    suspend fun delete(favoriteMovie: FavoriteMovieEntity)

    @Query("SELECT * FROM favorite_movies")
    fun selectAll(): Flow<List<FavoriteMovieEntity>>

    @Query("SELECT * FROM favorite_movies WHERE imdbId = :imdbId")
    fun selectById(imdbId: String): Flow<FavoriteMovieEntity?>
}
