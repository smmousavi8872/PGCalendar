package com.github.smmousavi.store

import app.cash.turbine.test
import com.github.smmousavi.common.result.Result
import com.github.smmousavi.domain.products.GetProductsUseCase
import com.github.smmousavi.model.Product
import com.github.smmousavi.model.Rating
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class ProductsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var mockGetProductsUseCase: GetProductsUseCase

    private lateinit var productsViewModel: ProductsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        productsViewModel = ProductsViewModel(mockGetProductsUseCase)
    }

    @Test
    fun getAllProducts_Success() = runTest {
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

        `when`(mockGetProductsUseCase.invoke()).thenReturn(flow {
            emit(Result.Loading)
            delay(100)
            emit(Result.Success(mockProducts))
        })

        productsViewModel.getAllProducts()

        productsViewModel.products.test {
            assertTrue(awaitItem() is Result.Loading)
            val successResult = awaitItem()
            assertTrue(successResult is Result.Success && successResult.data == mockProducts)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getAllProducts_Error() = runTest {
        val exception = RuntimeException("API error")
        `when`(mockGetProductsUseCase.invoke()).thenReturn(flow {
            emit(Result.Loading)
            delay(100)
            emit(Result.Error(exception))
        })

        productsViewModel.getAllProducts()
        productsViewModel.products.test {
            assertTrue(awaitItem() is Result.Loading)
            val errorResult = awaitItem()
            assertTrue(errorResult is Result.Error && errorResult.exception == exception)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun refreshProducts_Success() = runTest {
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
        `when`(mockGetProductsUseCase.invoke()).thenReturn(flow {
            emit(Result.Loading)
            delay(100)
            emit(Result.Success(mockProducts))
        })

        productsViewModel.refreshProducts()
        productsViewModel.products.test {
            assertTrue(awaitItem() is Result.Loading)
            val successResult = awaitItem()
            assert(successResult is Result.Success && successResult.data == mockProducts)
            cancelAndIgnoreRemainingEvents()
        }

        productsViewModel.isRefreshing.test {
            assertFalse(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun refreshProducts_Error() = runTest {
        val exception = RuntimeException("API error")
        `when`(mockGetProductsUseCase.invoke()).thenReturn(flow {
            emit(Result.Loading)
            delay(100)
            emit(Result.Error(exception))
        })

        productsViewModel.refreshProducts()
        productsViewModel.products.test {
            assertTrue(awaitItem() is Result.Loading)
            val errorResult = awaitItem()
            assertTrue(errorResult is Result.Error && errorResult.exception == exception)
            cancelAndIgnoreRemainingEvents()
        }

        productsViewModel.isRefreshing.test {
            assertFalse(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
