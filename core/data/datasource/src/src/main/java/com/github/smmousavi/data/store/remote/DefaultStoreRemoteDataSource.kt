package com.github.smmousavi.data.store.remote

import com.github.smmousavi.model.NetworkProduct
import com.github.smmousavi.network.apiservices.StoreApiService
import javax.inject.Inject

class DefaultStoreRemoteDataSource @Inject constructor(private val storeApiService: StoreApiService) :
    StoreRemoteDataSource {

    override suspend fun getAllProducts(): List<NetworkProduct> {
        return storeApiService.getAllProducts().data
    }
}