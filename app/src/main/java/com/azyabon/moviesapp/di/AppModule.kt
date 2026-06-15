package com.azyabon.moviesapp.di

import com.azyabon.moviesapp.data.remote.MovieApiService
import com.azyabon.moviesapp.data.remote.RetrofitInstance
import com.azyabon.moviesapp.data.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.content.Context
import androidx.room.Room
import com.azyabon.moviesapp.data.local.dao.FavoriteMovieDao
import com.azyabon.moviesapp.data.local.database.MovieDatabase
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieApiService(): MovieApiService {
        return RetrofitInstance.api
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        api: MovieApiService,
        favoriteMovieDao: FavoriteMovieDao,
    ): MovieRepository {
        return MovieRepository(api, favoriteMovieDao)
    }

    @Provides
    @Singleton
    fun provideMovieDatabase(
        @ApplicationContext context: Context
    ): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteMovieDao(
        database: MovieDatabase
    ): FavoriteMovieDao {
        return database.favoriteMovieDao()
    }
}