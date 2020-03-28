package com.kinley.repository.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single


@Dao
interface RxProductDao {

    @Insert
    fun insert(product: Product): Completable

    @Insert
    fun insertAll(products: List<Product>): Completable

    @Delete
    fun delete(product: Product)

    // One shot operation
    @Query("select * from product")
    fun getProductsInCart(): Single<List<Product>>


    // Stream of data
    @Query("select SUM( quantity * price ) from product")
    fun getCartAmount(): Observable<Double>

}
