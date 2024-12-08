package com.github.smmousavi.network.di

import androidx.tracing.trace
import com.github.smmousavi.network.apiservices.ProductsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal object ApiServiceModule {

    @Provides
    fun provideProductsApiService(retrofit: Retrofit): ProductsApiService {
        return trace("Trace:ProductRetrofit") { retrofit.create(ProductsApiService::class.java) }
    }
}