package com.vp.favorites.di

import com.vp.favorites.FavoriteActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FavoriteActivityModule {
    @ContributesAndroidInjector(modules = [FavoriteViewModelsModule::class])
    fun bindFavoriteActivity(): FavoriteActivity
}
