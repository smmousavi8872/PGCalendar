package com.github.smmousavi.store.di

import com.github.smmousavi.common.test.TestInitialize
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class StoreModuleTest {

    @Provides
    @TestInitialize
    fun provideInitializeProductsFlag(): Boolean = false

}