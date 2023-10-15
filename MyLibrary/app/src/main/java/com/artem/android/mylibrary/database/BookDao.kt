package com.artem.android.mylibrary.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.artem.android.mylibrary.Book
import java.util.UUID

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getBooks(): LiveData<List<Book>>
    @Query("SELECT * FROM book WHERE id=(:id)")
    fun getBook(id: UUID): LiveData<Book?>
    @Update
    fun updateBook(book: Book)
    @Insert
    fun addBook(book: Book)
}