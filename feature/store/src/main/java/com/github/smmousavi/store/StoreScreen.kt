package com.github.smmousavi.store

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.github.smmousavi.common.result.Result
import com.github.smmousavi.model.Product
import com.github.smmousavi.model.Rating
import com.github.smmousavi.ui.LoadingWheel
import com.github.smmousavi.ui.ProductList


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun StoreScreen(navController: NavHostController, viewModel: ProductsViewModel = hiltViewModel()) {
    val productsResult by viewModel.products.collectAsState()
    val refreshing by viewModel.isRefreshing.collectAsState()

    // Call getAllProducts() when the composable is first displayed
    LaunchedEffect(Unit) {
        viewModel.getAllProducts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.product_list)) },
                actions = {
                    IconButton(onClick = { navController.navigate("search") }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        val state = rememberPullRefreshState(
            refreshing = refreshing,
            onRefresh = { viewModel.refreshProducts() }
        )
        // Apply the padding values to the content
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .pullRefresh(state)
        ) {
            when (productsResult) {
                is Result.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LoadingWheel(contentDesc = stringResource(R.string.loading_products))
                    }
                }

                is Result.Success -> {
                    val products = (productsResult as Result.Success<List<Product>>).data
                    ProductList(products = products)
                }

                is Result.Error -> {
                    val message = (productsResult as Result.Error).exception.message
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = message ?: stringResource(R.string.an_error_occurred))
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = refreshing,
                state = state,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoreScreenPreview() {
    val sampleProducts = listOf(
        Product(
            id = 1,
            title = "Sample Product 1",
            price = 99.99,
            description = "This is a sample product description.",
            category = "Sample Category",
            image = "https://via.placeholder.com/150",
            rating = Rating(rate = 4.5, count = 100)
        ),
        Product(
            id = 2,
            title = "Sample Product 2",
            price = 49.99,
            description = "This is another sample product description.",
            category = "Sample Category",
            image = "https://via.placeholder.com/150",
            rating = Rating(rate = 3.5, count = 50)
        ),
        Product(
            id = 3,
            title = "Sample Product 3",
            price = 29.99,
            description = "This is another sample product description.",
            category = "Sample Category",
            image = "https://via.placeholder.com/150",
            rating = Rating(rate = 3.0, count = 30)
        ),
        Product(
            id = 4,
            title = "Sample Product 4",
            price = 19.99,
            description = "This is another sample product description.",
            category = "Sample Category",
            image = "https://via.placeholder.com/150",
            rating = Rating(rate = 2.5, count = 20)
        )
    )
    ProductList(products = sampleProducts)
}
