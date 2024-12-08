package com.github.smmousavi.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.github.smmousavi.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    // update or insert a products under the primary key
    @Upsert
    suspend fun upsertProducts(products: List<ProductEntity>)

    @Query("SELECT * FROM products")
    fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductById(id: Int): ProductEntity

    @Query(value = "DELETE FROM products WHERE id in (:ids)")
    suspend fun deleteProducts(ids: List<Int>)

    @Query("SELECT * FROM products WHERE title LIKE '%' || :query || '%'")
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    @Query("SELECT COUNT(*) FROM products")
    fun productsCount(): Int
}