package com.kinley.repository.koroutines

import com.kinley.repository.db.Product
import com.kinley.repository.db.ProductDao
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun insertAll(products: List<Product>)

    suspend fun insert(product: Product)

    suspend fun getProductsIncart(): List<Product>

    suspend fun getCartPriceFlow(): Flow<Double>
}

class ProductRepositoryImpl(
    private val productDao: ProductDao
) : ProductRepository {

    override suspend fun insertAll(products: List<Product>) {
        productDao.insertAll(products)
    }

    override suspend fun insert(product: Product) {
        productDao.insert(product)
    }

    override suspend fun getProductsIncart(): List<Product> = productDao.getProductsInCart()

    override suspend fun getCartPriceFlow(): Flow<Double> = productDao.getCartAmount()

}
