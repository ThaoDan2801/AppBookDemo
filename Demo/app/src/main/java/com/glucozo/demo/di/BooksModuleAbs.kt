package com.glucozo.book_market.di

import com.glucozo.book_market.user_manager.UserManagement
import com.glucozo.book_market.user_manager.UserManagementImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BooksModuleAbs {
    @Binds
    abstract fun provideUserManager(userManagementImpl: UserManagementImpl): UserManagement
}