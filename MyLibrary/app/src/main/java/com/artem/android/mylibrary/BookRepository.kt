package com.artem.android.mylibrary

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.artem.android.mylibrary.database.BookDatabase
import com.artem.android.mylibrary.database.migration_1_2
import java.io.File
import java.util.UUID
import java.util.concurrent.Executors

private const val DATABASE_NAME = "book-database"

class BookRepository private constructor(context: Context) {

    private val database : BookDatabase = Room.databaseBuilder(
            context.applicationContext,
            BookDatabase::class.java,
            DATABASE_NAME).addMigrations(migration_1_2).build()

    private val bookDao = database.bookDao()
    private val executor = Executors.newSingleThreadExecutor()
    private val filesDir = context.applicationContext.filesDir

    fun getBooks(): LiveData<List<Book>> = bookDao.getBooks()
    fun getBook(id: UUID): LiveData<Book?> = bookDao.getBook(id)
    fun updateBook(book: Book) {
        executor.execute {
            bookDao.updateBook(book)
        }
    }
    fun addBook(book: Book) {
        executor.execute {
            bookDao.addBook(book)
        }
    }

    fun getPhotoFile(book: Book): File = File(filesDir, book.photoFileName)

    companion object {
        private var INSTANCE: BookRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) INSTANCE = BookRepository(context)
        }

        fun get(): BookRepository {
            return INSTANCE ?: throw IllegalStateException("BookRepository must be initialized")
        }
    }
}
