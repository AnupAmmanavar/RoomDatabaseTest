package com.kinley.repository.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kinley.repository.aac.ProductDaoAAC

@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    abstract fun rxProductDao(): RxProductDao

    abstract fun productDaoAAC(): ProductDaoAAC
}
