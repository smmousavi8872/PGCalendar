package com.github.smmousavi.domain.products

import com.github.smmousavi.common.result.Result
import com.github.smmousavi.model.Product
import kotlinx.coroutines.flow.Flow

interface GetProductsUseCase {
    suspend operator fun invoke(): Flow<Result<List<Product>>>
}