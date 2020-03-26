package com.kinley.repository.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product: Product)

    @Insert
    suspend fun insertAll(products: List<Product>)


    @Delete
    suspend fun delete(product: Product)

    // One shot operation
    @Query("select * from product")
    suspend fun getProductsInCart(): List<Product>

    // Stream of data
    @Query("select SUM( quantity * price ) from product")
    fun getCartAmount(): Flow<Double>
}

/*

@Dao
interface RxProductDao {

    @Insert
    suspend fun insert(product: Product)

    @Delete
    suspend fun delete(product: Product)

    // One shot operation
    @Query("select * from product")
    fun getProductsInCart(): Single<List<Product>>


    // Stream of data
    @Query("select SUM( quantity * price ) from product")
    fun getCartAmount(): Observable<Double>

}
*/
