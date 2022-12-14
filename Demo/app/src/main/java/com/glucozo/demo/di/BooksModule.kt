package com.glucozo.book_market.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module(includes = [BooksModuleAbs::class])
@InstallIn(SingletonComponent::class)
object BooksModule {
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context
}