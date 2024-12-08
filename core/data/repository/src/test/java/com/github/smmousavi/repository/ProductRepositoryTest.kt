package com.github.smmousavi.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.github.smmousavi.common.result.Result
import com.github.smmousavi.database.SampleDatabase
import com.github.smmousavi.database.dao.ProductDao
import com.github.smmousavi.database.entity.ProductEntity
import com.github.smmousavi.database.entity.RatingEntity
import com.github.smmousavi.datasource.local.DefaultProductLocalDataSource
import com.github.smmousavi.datasource.local.ProductLocalDataSource
import com.github.smmousavi.datasource.remote.DefaultProductRemoteDataSource
import com.github.smmousavi.datasource.remote.ProductRemoteDataSource
import com.github.smmousavi.network.apiservices.ProductsApiService
import com.github.smmousavi.repository.product.DefaultOfflineFirstProductRepository
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ProductViewModelTest {

    private lateinit var fakeProductDao: ProductDao
    private lateinit var fakeLocalDataSource: ProductLocalDataSource
    private lateinit var fakeRemoteDataSource: ProductRemoteDataSource

    private lateinit var fakeProductRepository: DefaultOfflineFirstProductRepository
    private lateinit var fakeProductApiService: ProductsApiService
    private lateinit var mockWebServer: MockWebServer
    private lateinit var fakeDatabase: SampleDatabase

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        setUpInMemoryDatabase()
        setUpMockWebserver()

        fakeLocalDataSource = DefaultProductLocalDataSource(fakeProductDao)
        fakeRemoteDataSource = DefaultProductRemoteDataSource(fakeProductApiService)

        fakeProductRepository = DefaultOfflineFirstProductRepository(
            fakeLocalDataSource,
            fakeRemoteDataSource,
            testDispatcher
        )
    }

    private fun setUpInMemoryDatabase() {
        // Create an in-memory database
        fakeDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            SampleDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        fakeProductDao = fakeDatabase.productDao()
    }

    private fun setUpMockWebserver() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        fakeProductApiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(ProductsApiService::class.java)
    }

    // Unit Tests
    @After
    fun tearDown() {
        mockWebServer.shutdown()
        fakeDatabase.close()
    }

    @Test
    fun fetchProductsFromApi_Success() = runTest {
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

        fakeProductRepository.fetchAllProducts().test {
            assert(awaitItem() is Result.Loading)
            val successResult = awaitItem() as Result.Success
            assert(successResult.data[0].title == "Test Product")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun fetchProductsFromApi_Error() = runTest {
        val response = MockResponse()
            .setResponseCode(500)
            .setBody("""{ "error": "Internal Server Error" }""")
        mockWebServer.enqueue(response)

        fakeProductRepository.fetchAllProducts().test {
            assert(awaitItem() is Result.Loading)
            val errorResult = awaitItem()
            assert(errorResult is Result.Error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Integration tests
    @Test
    fun getAllProducts_OfflineFirst_Success() = runTest {
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

        val mockProducts = listOf(
            ProductEntity(
                1,
                "Test Product",
                100.0,
                "Description",
                "Category",
                "Image",
                RatingEntity(4.5, 100)
            )
        )
        fakeProductDao.upsertProducts(mockProducts)

        fakeProductRepository.getAllProducts().test {
            assertTrue(awaitItem() is Result.Loading)
            val successResult = awaitItem() as Result.Success
            assertEquals("Test Product", successResult.data[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getAllProducts_OfflineFirst_ErrorWithLocalFallback() = runTest {
        val mockProducts = listOf(
            ProductEntity(
                1,
                "Test Product",
                100.0,
                "Description",
                "Category",
                "Image",
                RatingEntity(4.5, 100)
            )
        )
        fakeProductDao.upsertProducts(mockProducts)

        val mockResponse = MockResponse()
            .setResponseCode(500)
            .setBody("""{ "error": "Internal Server Error" }""")
        mockWebServer.enqueue(mockResponse)

        fakeProductRepository.getAllProducts().test {
            assertTrue(awaitItem() is Result.Loading)
            val successResult = awaitItem() as Result.Success
            assertEquals("Test Product", successResult.data[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun searchProducts_Success() = runTest {
        val mockQuery = "Test"
        val mockProducts = listOf(
            ProductEntity(
                1,
                "Test Product",
                100.0,
                "Description",
                "Category",
                "Image",
                RatingEntity(4.5, 100)
            )
        )

        fakeProductDao.upsertProducts(mockProducts)

        fakeProductRepository.searchProducts(mockQuery).test {
            val result = awaitItem()
            assertEquals("Test Product", result[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
