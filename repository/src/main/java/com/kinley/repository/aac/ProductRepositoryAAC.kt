package com.kinley.repository.aac

import androidx.lifecycle.LiveData
import com.kinley.repository.db.Product

interface ProductRepositoryAAC {

    fun insert(product: Product)

    fun insertAll(products: List<Product>)

    fun getProductsIncart(): List<Product>

    fun getCartPriceLiveData(): LiveData<Double>
}


class ProductRepositoryAACImpl(
    private val dao: ProductDaoAAC
) : ProductRepositoryAAC {

    override fun insert(product: Product) {
        dao.insert(product)
    }

    override fun insertAll(products: List<Product>) {
        dao.insertAll(products)
    }

    override fun getProductsIncart(): List<Product> {
        return dao.getProductsInCart()
    }

    override fun getCartPriceLiveData(): LiveData<Double> {
        return dao.getCartAmount()
    }

}
