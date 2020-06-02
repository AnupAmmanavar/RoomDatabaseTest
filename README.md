# Testing the Database layer

## Why the data layer should be first place to start your testing?
* Whichever architecture you choose, be it the MVVM, MVP, MVC, or RIBS, the data-layer is the one that goes through minimal changes. It almost remains the same when there are architectural migrations.
* The data-layer has minimal dependencies, which makes it very easier to test.
* It can be unit-tested using Robolectric.

## What is Data-layer and types of data?

* It is the layer responsible for providing the data for the app, through network requests and local persistence. It forms the basis on which the presentation/business logic is written.
* The presentation/business layer consumes the data. There are two ways through which it can request the data namely __One-shot data__ and __Stream of data__.

>The data-layer includes the local persistence and the remote service. In this tutorial, we will ignore remote service for demonstration purposes. However, in the end, you will be able to write it by yourself.

We cover adding test cases for Androids __LiveData__, __RxJava/RxKotlin__, and __Coroutine__. You can directly skip to that section which you are using in your app.


*Demonstration example*:- We will consider a section of the shopping application where you get the List of Products. You can add or remove the products from the Shopping Cart. The real-time Cart Amount is also shown.

1. List of Products → *one-shot operation*. For this example, you cannot remove the items from the cart. You can either increment or decrement the quantities.
2. The real-time Cart Amount → *a stream-of-data*. It changes as and when the item quantities are altered(increment/decrement).

![](https://miro.medium.com/max/1400/1*6Km2pQ2-vzJzJr1gwY4nHA.png)



## 1. Room database using RxJava

### Creating DAO(Data access object):-
* Inserting is a __Completable__ operation.
* One-shot-operation of products retrieval is a __Single__ operation.
* Data-stream of Cart amount is an __Observable__.

```kotlin
@Dao
interface RxProductDao {

    @Insert
    fun insertAll(products: List<Product>): Completable

    // One shot operation
    @Query("select * from product")
    fun getProductsInCart(): Single<List<Product>>

    // Stream of data
    @Query("select SUM( quantity * price ) from product")
    fun getCartAmount(): Observable<Double>
}
```

### Testing in RxJava
RxJava test utils provides a `.test()` method. It creates a `TestObserver` on the data source and subscribes to it. Thus, on subscription, you get the value from the data source immediately and consequently, you can check the value. Below example makes it clear


#### Testing one-shot operation of fetching the List of products 

```kotlin
class RxProductDaoTest {

  lateinit var repository: RxProductDao
  ..
  ..

  @Test
  fun getProductsSingleTest() {
    // 1. Create 5 products and insert them all
    val testProducts = DataProvider.getProducts(5)
    repository.insertAll(testProducts).test()
    
    // 2 . Fetch the products and check whether all the 5 products inserted are retrieved
    repository.getProductsIncart().test()
      .assertValue { cachedProducts ->
        areContentsSame(testProducts, cachedProducts)
      }
    
  }
  
  private areContentsSame(list1: List<Product>, list2: List<Product>): Boolean { .. }
  
}
```

#### Testing stream of data for realtime CartAmount

```kotlin
class RxProductDaoTest {

  lateinit var repository: RxProductDao
  ..
  ..

    @Test
    fun getCartAmountObservableTest() {
        // 1. Create 5 test products and insert them
        val testProducts = DataProvider.getProducts(5)
        repository.insertAll(testProducts).test()

        // 2. Calculate the expected price
        var expectedPrice = 0.0
        testProducts.forEach { expectedPrice += it.price * it.quantity }

        // 3. Check that expected and the actual price are the same
        repository.getCartAmountObservable().test().assertValue { it == expectedPrice }

        // 4. Add another product into the repository
        val testProduct = DataProvider.getProduct(6)
        repository.insert(testProduct).test()

        // 5. Calculate the updated price
        val updatePrice = expectedPrice + (testProduct.quantity * testProduct.price)

        // 6. Check that expected and the actual price are the same
        repository.getCartPriceFlow().test().assertValue { it == updatePrice }
    }  
}
```

## 2. Room database using Coroutines
Room has support for coroutines. They run on the custom dispatcher. Coroutines are famous for their sequential nature. But the only condition being the functions have to be `suspend` function.

### Creating DAO(Data access object)
* One-shot operation of fetching the products is a `suspend` function.
* Cart amount which is stream-of-data is declared as `Flow` of Double.

```kotlin
@Dao
interface CoroutinesProductDao {

    @Insert
    suspend fun insertAll(products: List<Product>)

    // One shot operation
    @Query("select * from product")
    suspend fun getProductsInCart(): List<Product>

    // Stream of data
    @Query("select SUM( quantity * price ) from product")
    fun getCartAmount(): Flow<Double>
}
```

### Testing in Room-ktx
`runBlocking` runs a new coroutine and blocks the current thread interruptible until its completion. Hence all the tests should be encapsulated inside this block which ensures that the tests run to completion.

#### Testing one-shot operation
```kotlin
class CoroutinesProductDaoTest {

    private lateinit var productRepository: CoroutinesProductDao
    ..
    ..

    @Test
    fun insertAll() {
        runBlocking {
            // 1. create 5 test products and insert them in the repository
            val testProducts = DataProvider.getProducts(5)
            productRepository.insertAll(testProducts)

            // 2. Fetch the products from the repository
            val cachedProducts = productRepository.getProductsIncart()

            // 3. Check whether the fetched and expected products are equal
            assertEquals(testProducts, cachedProducts)
        }
    }

}
```

#### Testing stream of Data

```kotlin
class CoroutinesProductDaoTest {

    private lateinit var productRepository: CoroutinesProductDao
    ..
    ..


    @Test
    fun getCartPrice() {
        runBlocking {
            // 1. create 5 test products and insert them in the repository
            val testProducts = DataProvider.getProducts(5)
            productRepository.insertAll(testProducts)

            // 2. Calculate the expected price
            var expectedPrice = 0.0
            testProducts.forEach { expectedPrice += it.price * it.quantity }

            // 3. Fetch the expected price and check whether it matches the expected value
            val price = productRepository.getCartPriceFlow().take(1).toList()[0]
            assert(expectedPrice == price)

            // 4. Add another product
            val testProduct = DataProvider.getProduct(6)
            productRepository.insert(testProduct)

            // 5. Calculate the expected price
            val updatePrice = expectedPrice + (testProduct.quantity * testProduct.price)

            // 6. Check the expected and actual value
            assert(updatePrice == productRepository.getCartPriceFlow().take(1).toList()[0])

        }
    }

}
```

## 3. Using Android’s Architecture components LiveData
* Inserting and Fetching the products are normal functions. Make sure to run it off the main thread.
* Cart Amount which is a stream of data is declared as a `LiveData`.

```kotlin
@Dao
interface ProductDaoAAC {

    @Insert
    fun insertAll(products: List<Product>)

    @Query("select * from product")
    fun getProductsInCart(): List<Product>

    @Query("select SUM( quantity * price ) from product")
    fun getCartAmount(): LiveData<Double>
}
```

### Testing LiveData
* For one-shot operation make a normal call, making sure to run off the main thread.
* For a stream of data, we need to subscribe to live data. Because it won’t emit the values unless there are active observers on it. We have an extension function called `getOrAwait()` on the `LiveData` which gives us the value of the LiveData instantaneously. It’s borrowed from (here)[https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/test-common/java/com/android/example/github/util/LiveDataTestUtil.kt].

```kotlin
class ProductRepositoryAACImplTest { 

  lateinit var repository: ProductDaoAAC
  ..
  ..
  
  @Test
    fun getCartAmountLiveDataTest() {
    
        // 1. create 5 test products and insert them in the repository
        val testProducts = DataProvider.getProducts(5)
        repository.insertAll(testProducts)

        //  2. Calculate the expected price
        var expectedPrice = 0.0
        testProducts.forEach { expectedPrice += it.price * it.quantity }

        // 3. Fetch the expected price and check whether it matches the expected value
        // getOrAwait() extension function returns us the value by providing a test active subscriber
        var price = repository.getCartPriceLiveData().getOrAwaitValue()
        assertEquals(expectedPrice, price, 0.0)

        // 4. Add another product
        val anotherProduct = DataProvider.getProduct(6)
        repository.insert(anotherProduct)

        // 5. Calculate the expected price
        expectedPrice += anotherProduct.price * anotherProduct.quantity

        // 6. Fetch the Cart amount again. And check the expected and actual value
        price = repository.getCartPriceLiveData().getOrAwaitValue()
        
        assertEquals(expectedPrice, price, 0.0)

    }

}
```










