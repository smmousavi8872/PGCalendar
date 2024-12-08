package com.github.smmousavi.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.smmousavi.model.Product
import com.github.smmousavi.model.Rating


@Composable
fun ProductList(products: List<Product>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(8.dp)
    ) {
        items(products.size) { index ->
            Card(
                modifier = Modifier
                    .padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                ProductItem(products[index])
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListPreview() {
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
