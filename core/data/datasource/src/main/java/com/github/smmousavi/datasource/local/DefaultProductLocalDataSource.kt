package com.github.smmousavi.datasource.local

import com.github.smmousavi.database.dao.ProductDao
import com.github.smmousavi.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultProductLocalDataSource @Inject constructor(private val productDao: ProductDao) :
    ProductLocalDataSource {
    override suspend fun upsertProducts(products: List<ProductEntity>) {
        productDao.upsertProducts(products)
    }

    override suspend fun getAllProducts(): List<ProductEntity> {
        return productDao.getAllProducts()
    }

    override suspend fun getProductById(id: Int): ProductEntity {
        return productDao.getProductById(id)
    }

    override suspend fun deleteProducts(ids: List<Int>) {
        productDao.deleteProducts(ids)
    }

    override suspend fun searchProducts(query: String): Flow<List<ProductEntity>> {
        return productDao.searchProducts(query)
    }

    override suspend fun productsCount(): Int {
        return productDao.productsCount()
    }
}