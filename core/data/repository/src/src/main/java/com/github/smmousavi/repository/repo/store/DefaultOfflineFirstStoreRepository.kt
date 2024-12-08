package com.github.smmousavi.repository.repo.store

import com.github.smmousavi.data.store.remote.DefaultStoreRemoteDataSource
import com.github.smmousavi.model.Product
import javax.inject.Inject

class DefaultOfflineFirstStoreRepository @Inject constructor(
    private val storeRemoteDataSource: DefaultStoreRemoteDataSource,
) :
    OfflineFirstStoreRepository {
    override suspend fun getAllProducts(): List<Product> {
        return storeRemoteDataSource.getAllProducts()
    }
}