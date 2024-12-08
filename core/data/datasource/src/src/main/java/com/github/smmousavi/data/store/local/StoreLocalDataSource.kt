package com.github.smmousavi.data.store.local

import com.github.smmousavi.model.NetworkProduct
import com.github.smmousavi.model.Product

interface StoreLocalDataSource {

    suspend fun upsertProducts(products: List<NetworkProduct>)

    suspend fun getAllProducts(): List<Product>

}