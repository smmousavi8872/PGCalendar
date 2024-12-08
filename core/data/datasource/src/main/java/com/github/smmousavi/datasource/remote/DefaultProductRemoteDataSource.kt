package com.github.smmousavi.datasource.remote

import com.github.smmousavi.network.apiservices.ProductsApiService
import com.github.smmousavi.network.response.ProductResponse
import javax.inject.Inject

class DefaultProductRemoteDataSource @Inject constructor(
    private val productApiService: ProductsApiService,
) : ProductRemoteDataSource {

    override suspend fun requestAllProducts(): List<ProductResponse> {
        return productApiService.requestAllProducts()
    }
}