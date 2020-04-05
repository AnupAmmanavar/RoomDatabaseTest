package com.kinley.repository.aac

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kinley.repository.db.Product

@Dao
interface ProductDaoAAC {

    @Insert
    fun insert(product: Product)

    @Insert
    fun insertAll(products: List<Product>)

    @Query("select * from product")
    fun getProductsInCart(): List<Product>

    @Query("select SUM( quantity * price ) from product")
    fun getCartAmount(): LiveData<Double>
}
