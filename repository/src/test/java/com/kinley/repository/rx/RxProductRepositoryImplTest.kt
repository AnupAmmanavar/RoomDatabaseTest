package com.kinley.repository.rx

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kinley.repository.datafactory.DataProvider
import com.kinley.repository.db.AppDatabase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class RxProductRepositoryImplTest {

    private lateinit var repository: RxProductRepository

    @JvmField
    @Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val roomTestRule: RoomTestRule = RoomTestRule()

    private val db: AppDatabase = roomTestRule.db

    @Before
    fun setUp() {
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
}
