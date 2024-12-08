package com.github.smmousavi.domain.search

import com.github.smmousavi.model.Product
import kotlinx.coroutines.flow.Flow

interface SearchProductsUseCase {

    suspend operator fun invoke(query: String): Flow<List<Product>>

}