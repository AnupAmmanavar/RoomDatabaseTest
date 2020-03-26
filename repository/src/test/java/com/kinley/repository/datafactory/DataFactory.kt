package com.kinley.repository.datafactory

import android.os.Build
import com.kinley.repository.db.Product
import java.util.*
import java.util.concurrent.ThreadLocalRandom

object DataProvider {

    fun getProducts(count: Int): List<Product> {
        val products = mutableListOf<Product>()
        for (i in 0..count) {
            products.add(getProduct(id = i))
        }
        return products
    }

    fun getProduct(id: Int): Product = Product(
        id = id,
        name = df.randomString(),
        price = df.randomDouble(),
        quantity = df.randomInt()
    )
}


object DataFactory {

    fun randomString(): String {
        return UUID.randomUUID().toString()
    }

    fun randomInt(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ThreadLocalRandom.current().nextInt(0, 1000 + 1)
        } else {
            Random().nextInt(1000 + 1)
        }
    }

    fun randomDouble(): Double {
        return randomInt().toDouble()
    }
}

typealias df = DataFactory
