package com.github.smmousavi.datasource.remote

import com.github.smmousavi.network.response.ProductResponse

interface ProductRemoteDataSource {

    suspend fun requestAllProducts(): List<ProductResponse>
}