package com.github.smmousavi.domain

import app.cash.turbine.test
import com.github.smmousavi.common.result.Result
import com.github.smmousavi.domain.products.DefaultGetProductsUseCase
import com.github.smmousavi.model.Product
import com.github.smmousavi.model.Rating
import com.github.smmousavi.repository.product.DefaultOfflineFirstProductRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetProductsUseCaseTest {
    @Mock
    private lateinit var mockProductsRepository: DefaultOfflineFirstProductRepository

    private lateinit var getProductsUseCase: DefaultGetProductsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getProductsUseCase = DefaultGetProductsUseCase(mockProductsRepository)
    }

    @Test
    fun invoke_Success() = runTest {
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
        `when`(mockProductsRepository.getAllProducts()).thenReturn(
            flowOf(
                Result.Success(
                    mockProducts
                )
            )
        )

        getProductsUseCase().test {
            val result = awaitItem()
            assert(result is Result.Success && result.data == mockProducts)
            awaitComplete()
        }
    }

    @Test
    fun invoke_Error() = runTest {
        val exception = RuntimeException("API error")
        `when`(mockProductsRepository.getAllProducts()).thenReturn(
            flowOf(
                Result.Error(
                    exception
                )
            )
        )

        getProductsUseCase().test {
            val result = awaitItem()
            assert(result is Result.Error && result.exception == exception)
            awaitComplete()
        }
    }
}