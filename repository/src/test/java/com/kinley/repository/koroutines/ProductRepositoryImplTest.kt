@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kinley.repository.koroutines

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.kinley.repository.datafactory.DataProvider
import com.kinley.repository.db.AppDatabase
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class ProductRepositoryImplTest {

    private lateinit var db: AppDatabase

    private lateinit var productRepository: ProductRepository

    @JvmField
    @Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries()
            .build()

        productRepository = ProductRepositoryImpl(db.productDao())
    }

    @Test
    fun insert() {
        runBlocking {
            val testProduct = DataProvider.getProduct(1)
            productRepository.insert(testProduct)

            val cachedProducts = productRepository.getProductsIncart()

            assert(cachedProducts.size == 1)

            assertEquals(cachedProducts[0], testProduct)
        }
    }

    @Test
    fun insertAll() {
        runBlocking {

            val testProducts = DataProvider.getProducts(5)
            productRepository.insertAll(testProducts)

            val cachedProducts = productRepository.getProductsIncart()

            assertEquals(testProducts, cachedProducts)
        }
    }


    @Test
    fun getCartPrice() {
        runBlocking {
            val testProducts = DataProvider.getProducts(5)
            productRepository.insertAll(testProducts)

            var expectedPrice = 0.0
            testProducts.forEach { expectedPrice += it.price * it.quantity }

            val price = productRepository.getCartPriceFlow().take(1).toList()[0]

            assert(expectedPrice == price)

            // Add another product
            val testProduct = DataProvider.getProduct(6)
            productRepository.insert(testProduct)

            val updatePrice = expectedPrice + (testProduct.quantity * testProduct.price)

            assert(updatePrice == productRepository.getCartPriceFlow().take(1).toList()[0])

        }
    }

    @After
    fun tearDown() {
        db.close()
    }
}
