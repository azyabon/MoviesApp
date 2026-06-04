package com.azyabon.moviesapp.di

import com.azyabon.moviesapp.data.remote.MovieApiService
import com.azyabon.moviesapp.data.remote.RetrofitInstance
import com.azyabon.moviesapp.data.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
        api: MovieApiService
    ): MovieRepository {
        return MovieRepository(api)
    }
}