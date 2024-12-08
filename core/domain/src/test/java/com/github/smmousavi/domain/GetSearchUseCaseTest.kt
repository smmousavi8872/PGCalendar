package com.github.smmousavi.domain

import app.cash.turbine.test
import com.github.smmousavi.domain.search.DefaultSearchProductsUseCase
import com.github.smmousavi.model.Product
import com.github.smmousavi.model.Rating
import com.github.smmousavi.repository.product.DefaultOfflineFirstProductRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetSearchUseCaseTest {

    @Mock
    private lateinit var mockProductsRepository: DefaultOfflineFirstProductRepository

    private lateinit var searchProductsUseCase: DefaultSearchProductsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        searchProductsUseCase = DefaultSearchProductsUseCase(mockProductsRepository)
    }

    @Test
    fun invoke_Success() = runTest {
        val testQuery = "Test"
        val mockProducts = listOf(
            Product(
                1,
                "Test Product",
                100.0,
                "Description",
                "Category",
                "Image",
                Rating(4.5, 100)
            )
        )
        `when`(mockProductsRepository.searchProducts(query = testQuery)).thenReturn(
            flowOf(
                mockProducts
            )
        )

        searchProductsUseCase(testQuery).test {
            assertEquals(mockProducts, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when invoking with empty query should return empty list`() = runTest {
        val testQuery = ""
        val mockProduct = emptyList<Product>()
        `when`(mockProductsRepository.searchProducts(testQuery)).thenReturn(
            flowOf(
                mockProduct
            )
        )

        searchProductsUseCase(testQuery).test {
            assertEquals(mockProduct, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

    }
}