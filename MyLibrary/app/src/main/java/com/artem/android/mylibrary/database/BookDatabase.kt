package com.artem.android.mylibrary.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.artem.android.mylibrary.Book

@Database(entities = [ Book::class ], version=1)
@TypeConverters(BookTypeConverters::class)
abstract class BookDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
}