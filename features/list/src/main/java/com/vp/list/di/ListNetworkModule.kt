package com.vp.list.di

import com.vp.list.service.SearchService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object ListNetworkModule {
    @Provides
    fun providesSearchService(retrofit: Retrofit): SearchService =
        retrofit.create(SearchService::class.java)
}
