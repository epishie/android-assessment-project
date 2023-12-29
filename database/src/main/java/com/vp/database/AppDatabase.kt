package com.vp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vp.database.dao.FavoriteMoviesDao
import com.vp.database.entity.FavoriteMovieEntity

@Database(entities = [FavoriteMovieEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteMoviesDao(): FavoriteMoviesDao
}
