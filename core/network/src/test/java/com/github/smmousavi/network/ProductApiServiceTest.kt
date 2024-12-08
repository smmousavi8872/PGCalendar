package com.github.smmousavi.network

import com.github.smmousavi.network.apiservices.ProductsApiService
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductApiServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ProductsApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(ProductsApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `requestAllProducts should return a list of products`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                """[
                {
                    "id": 1,
                    "title": "Test Product",
                    "price": 100.0,
                    "description": "Description",
                    "category": "Category",
                    "image": "Image",
                    "rating": { "rate": 4.5, "count": 100 }
                }
            ]"""
            )
        mockWebServer.enqueue(mockResponse)

        val products = apiService.requestAllProducts()

        assertEquals(1, products.size)
        assertEquals("Test Product", products[0].title)
    }
}