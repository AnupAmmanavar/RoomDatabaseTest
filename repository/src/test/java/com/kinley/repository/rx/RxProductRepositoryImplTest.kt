package com.kinley.repository.rx

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.kinley.repository.datafactory.DataProvider
import com.kinley.repository.db.AppDatabase
import com.kinley.repository.koroutines.ProductRepository
import com.kinley.repository.koroutines.ProductRepositoryImpl
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class RxProductRepositoryImplTest {

    private lateinit var db: AppDatabase

    private lateinit var repository: RxProductRepository

    @JvmField
    @Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries()
            .build()

        repository = RxProductRepositoryImpl(db.rxProductDao())
    }

    @Test
    fun insert() {
        val testProduct = DataProvider.getProduct(1)
        repository.insert(testProduct).test()

        repository.getProductsIncart().test()
            .assertValue { testProduct == it[0] }
    }

    @Test
    fun getCartPriceFlow() {
        val testProducts = DataProvider.getProducts(5)
        repository.insertAll(testProducts).test()

        var expectedPrice = 0.0
        testProducts.forEach { expectedPrice += it.price * it.quantity }

        repository.getCartPriceFlow().test().assertValue { it == expectedPrice }

        // Add another product
        val testProduct = DataProvider.getProduct(6)
        repository.insert(testProduct).test()

        val updatePrice = expectedPrice + (testProduct.quantity * testProduct.price)

        repository.getCartPriceFlow().test().assertValue { it == updatePrice }
    }

    @After
    fun tearDown() {
        db.close()
    }
}
