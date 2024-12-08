package com.github.smmousavi.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.smmousavi.database.dao.ProductDao
import com.github.smmousavi.database.entity.ProductEntity

@Database(entities = [ProductEntity::class], version = 1, exportSchema = true)
abstract class SampleDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

}