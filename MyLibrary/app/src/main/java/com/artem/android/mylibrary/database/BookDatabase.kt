package com.artem.android.mylibrary.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.artem.android.mylibrary.Book

@Database(entities = [ Book::class ], version=2)
@TypeConverters(BookTypeConverters::class)
abstract class BookDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
}

val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE Book ADD COLUMN rating TEXT NOT NULL DEFAULT ''"
        )
    }
}