package com.azyabon.moviesapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.azyabon.moviesapp.data.local.dao.FavoriteMovieDao
import com.azyabon.moviesapp.data.local.entity.FavoriteMovieEntity

@Database(
    entities = [FavoriteMovieEntity::class],
    version = 1
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao
}