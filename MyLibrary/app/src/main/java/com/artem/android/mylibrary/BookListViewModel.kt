package com.artem.android.mylibrary

import androidx.lifecycle.ViewModel

class BookListViewModel: ViewModel() {
    private val bookRepository = BookRepository.get()
    val booksListLiveData = bookRepository.getBooks()

    fun addBook(book: Book) { bookRepository.addBook(book) }
}