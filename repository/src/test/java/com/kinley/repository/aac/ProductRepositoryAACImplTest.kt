package com.kinley.repository.aac

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.kinley.repository.datafactory.DataProvider
import com.kinley.repository.db.AppDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class ProductRepositoryAACImplTest {

    private lateinit var db: AppDatabase

    private lateinit var repository: ProductRepositoryAAC

    @JvmField
    @Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries()
            .build()

        repository = ProductRepositoryAACImpl(db.productDaoAAC())
    }

    @Test
    fun insertProducts() {
        val testProducts = DataProvider.getProducts(5)
        repository.insertAll(testProducts)

        val cacheProducts = repository.getProductsIncart()

        assertEquals(testProducts, cacheProducts)

    }

    @Test
    fun getCartAmount() {
        val testProducts = DataProvider.getProducts(5)
        repository.insertAll(testProducts)

        var expectedPrice = 0.0
        testProducts.forEach { expectedPrice += it.price * it.quantity }

        var price = repository.getCartPriceLiveData().getOrAwaitValue()

        assertEquals(expectedPrice, price, 0.0)

        val anotherProduct = DataProvider.getProduct(6)
        repository.insert(anotherProduct)

        expectedPrice += anotherProduct.price * anotherProduct.quantity

        price = repository.getCartPriceLiveData().getOrAwaitValue()

        assertEquals(expectedPrice, price, 0.0)


    }

    @After
    fun tearDown() {
        db.close()
    }
}

// copied from https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/test-common/java/com/android/example/github/util/LiveDataTestUtil.kt
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    afterObserve.invoke()

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        this.removeObserver(observer)
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}
