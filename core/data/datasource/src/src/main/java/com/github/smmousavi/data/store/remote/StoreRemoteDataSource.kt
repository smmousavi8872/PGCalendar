package com.github.smmousavi.data.store.remote

import com.github.smmousavi.model.NetworkProduct

interface StoreRemoteDataSource {

    suspend fun getAllProducts(): List<NetworkProduct>

}