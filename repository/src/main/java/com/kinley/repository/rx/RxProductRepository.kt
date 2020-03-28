package com.kinley.repository.rx

import com.kinley.repository.db.Product
import com.kinley.repository.db.RxProductDao
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface RxProductRepository {


    fun insertAll(products: List<Product>): Completable

    fun insert(product: Product) : Completable

    fun getProductsIncart(): Single<List<Product>>

    fun getCartPriceFlow(): Observable<Double>

}

class RxProductRepositoryImpl(
    private val dao: RxProductDao
) : RxProductRepository {

    override fun insertAll(products: List<Product>): Completable {
        return dao.insertAll(products)
    }

    override fun insert(product: Product): Completable {
        return  dao.insert(product)
    }

    override fun getProductsIncart(): Single<List<Product>> {
        return dao.getProductsInCart()
    }

    override fun getCartPriceFlow(): Observable<Double> {
        return dao.getCartAmount()
    }

}
