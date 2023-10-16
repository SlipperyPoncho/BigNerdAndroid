package com.artem.android.mylibrary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import java.io.File
import java.util.UUID

class BookDetailViewModel: ViewModel() {

    private val bookRepository = BookRepository.get()
    private val bookIdLiveData = MutableLiveData<UUID>()

    var bookLiveData: LiveData<Book?> = bookIdLiveData.switchMap {
        bookId -> bookRepository.getBook(bookId)
    }

    fun loadBook(bookId: UUID) {
        bookIdLiveData.value = bookId
    }

    fun saveBook(book: Book) {
        bookRepository.updateBook(book)
    }

    fun getPhotoFile(book: Book): File {
        return bookRepository.getPhotoFile(book)
    }
}