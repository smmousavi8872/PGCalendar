package com.github.smmousavi.repository.product

import com.github.smmousavi.asEntity
import com.github.smmousavi.asExternalModel
import com.github.smmousavi.common.network.AppDispatchers
import com.github.smmousavi.common.network.Dispatcher
import com.github.smmousavi.common.result.Result
import com.github.smmousavi.datasource.local.ProductLocalDataSource
import com.github.smmousavi.datasource.remote.ProductRemoteDataSource
import com.github.smmousavi.model.Product
import com.github.smmousavi.network.response.ProductResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultOfflineFirstProductRepository @Inject constructor(
    private val productLocalDataSource: ProductLocalDataSource,
    private val productRemoteDataSource: ProductRemoteDataSource,
    @Dispatcher(AppDispatchers.IO) val ioDispatcher: CoroutineDispatcher,
) : OfflineFirstProductRepository {

    override suspend fun fetchAllProducts(): Flow<Result<List<ProductResponse>>> = flow {
        emit(Result.Loading)
        val networkProducts = productRemoteDataSource.requestAllProducts()
        emit(Result.Success(networkProducts))
    }
        .catch { e -> emit(Result.Error(e)) }

    // Offline first approach to conserve the Single Source of Truth principle
    override suspend fun getAllProducts(): Flow<Result<List<Product>>> = flow {
        fetchAllProducts().collect { result ->
            when (result) {
                is Result.Loading -> emit(Result.Loading)

                is Result.Success -> {
                    productLocalDataSource.upsertProducts(result.data.map { it.asEntity() })
                    emit(
                        Result.Success(
                            productLocalDataSource.getAllProducts()
                                .map { it.asExternalModel() })
                    )
                }

                is Result.Error -> {
                    if (productLocalDataSource.productsCount() > 0) {
                        emit(
                            Result.Success(
                                productLocalDataSource.getAllProducts()
                                    .map { it.asExternalModel() })
                        )
                    } else {
                        emit(Result.Error(result.exception))
                    }
                }
            }
        }
    }
        .catch { e -> emit(Result.Error(e)) }
        .flowOn(ioDispatcher)

    override suspend fun searchProducts(query: String): Flow<List<Product>> {
        return productLocalDataSource.searchProducts(query)
            .map { list -> list.map { it.asExternalModel() } }
    }
}