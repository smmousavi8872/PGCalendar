package com.github.smmousavi.di

import com.github.smmousavi.repository.product.DefaultOfflineFirstProductRepository
import com.github.smmousavi.repository.product.OfflineFirstProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsOfflineFirstProductRepository(productRepository: DefaultOfflineFirstProductRepository): OfflineFirstProductRepository
}