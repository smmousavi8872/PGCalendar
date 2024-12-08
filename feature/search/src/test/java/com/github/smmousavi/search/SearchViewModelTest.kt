package com.github.smmousavi.search

import app.cash.turbine.test
import com.github.smmousavi.domain.search.SearchProductsUseCase
import com.github.smmousavi.model.Product
import com.github.smmousavi.model.Rating
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var searchProductsUseCase: SearchProductsUseCase

    private lateinit var searchViewModel: SearchViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        searchViewModel = SearchViewModel(searchProductsUseCase)
    }

    @Test
    fun `when queried a product, a list of products should be returned`() =
        runTest {
            val mockQuery = "Test"
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
            `when`(searchProductsUseCase.invoke(mockQuery)).thenReturn(flowOf(mockProducts))
            searchViewModel.searchProducts()
            searchViewModel.updateSearchQuery(mockQuery)

            // Add delay to allow debounce to work
            delay(500)

            searchViewModel.searchResults.test {
                val results = awaitItem()
                assertEquals(mockProducts, results)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when an empty query is entered, an empty list should be returned`() = runTest {
        searchViewModel.searchProducts()
        searchViewModel.updateSearchQuery("")

        // Add delay to allow debounce to work
        delay(500)

        searchViewModel.searchResults.test {
            val result = awaitItem()
            assert(result.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when debouncing characters, the final input should be queried`() = runTest {
        val mockQuery = "Test"
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
        `when`(searchProductsUseCase.invoke(mockQuery)).thenReturn(flowOf(mockProducts))

        searchViewModel.searchProducts()
        searchViewModel.updateSearchQuery("T")
        searchViewModel.updateSearchQuery("Te")
        searchViewModel.updateSearchQuery("Tes")
        searchViewModel.updateSearchQuery("Test")

        // Add delay to allow debounce to work
        delay(500)

        searchViewModel.searchResults.test {
            assertEquals(mockProducts, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}