package com.github.smmousavi.repository.repo.store

import com.github.smmousavi.model.Product

interface OfflineFirstStoreRepository {

    suspend fun getAllProducts(): List<Product>

}