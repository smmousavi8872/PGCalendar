package com.github.smmousavi.datasource.local

import com.github.smmousavi.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

interface ProductLocalDataSource {

    suspend fun upsertProducts(products: List<ProductEntity>)

    suspend fun getAllProducts(): List<ProductEntity>

    suspend fun getProductById(id: Int): ProductEntity

    suspend fun deleteProducts(ids: List<Int>)

    suspend fun searchProducts(query: String): Flow<List<ProductEntity>>

    suspend fun productsCount(): Int
}