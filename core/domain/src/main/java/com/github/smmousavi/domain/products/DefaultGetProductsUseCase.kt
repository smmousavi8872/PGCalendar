package com.github.smmousavi.domain.products

import com.github.smmousavi.repository.product.OfflineFirstProductRepository
import javax.inject.Inject

class DefaultGetProductsUseCase @Inject constructor(private val productsRepository: OfflineFirstProductRepository) :
    GetProductsUseCase {
    override suspend operator fun invoke() = productsRepository.getAllProducts()
}