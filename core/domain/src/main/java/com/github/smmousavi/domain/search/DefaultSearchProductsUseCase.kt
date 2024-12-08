package com.github.smmousavi.domain.search

import com.github.smmousavi.repository.product.OfflineFirstProductRepository
import javax.inject.Inject

class DefaultSearchProductsUseCase @Inject constructor(private val productsRepository: OfflineFirstProductRepository) :
    SearchProductsUseCase {
    override suspend operator fun invoke(query: String) = productsRepository.searchProducts(query)

}