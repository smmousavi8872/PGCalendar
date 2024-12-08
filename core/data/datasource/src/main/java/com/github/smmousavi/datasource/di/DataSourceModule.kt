package com.github.smmousavi.datasource.di

import com.github.smmousavi.datasource.local.DefaultProductLocalDataSource
import com.github.smmousavi.datasource.local.ProductLocalDataSource
import com.github.smmousavi.datasource.remote.DefaultProductRemoteDataSource
import com.github.smmousavi.datasource.remote.ProductRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindsProductLocalDataSource(localDataSource: DefaultProductLocalDataSource): ProductLocalDataSource

    @Binds
    abstract fun bindsRemoteDataSource(remoteDataSource: DefaultProductRemoteDataSource): ProductRemoteDataSource

}