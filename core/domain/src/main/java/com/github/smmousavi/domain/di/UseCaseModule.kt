package com.github.smmousavi.domain.di

import com.github.smmousavi.domain.products.DefaultGetProductsUseCase
import com.github.smmousavi.domain.products.GetProductsUseCase
import com.github.smmousavi.domain.search.DefaultSearchProductsUseCase
import com.github.smmousavi.domain.search.SearchProductsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindsGetProductsUseCase(getProductsUseCase: DefaultGetProductsUseCase): GetProductsUseCase

    @Binds
    abstract fun bindsSearchProductsUseCase(searchProductsUseCase: DefaultSearchProductsUseCase): SearchProductsUseCase
}