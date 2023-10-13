package com.artem.android.mylibrary

import androidx.lifecycle.ViewModel

class BookListViewModel: ViewModel() {
    val books = mutableListOf<Book>()

    init {
        for (i in 0 until 100) {
            val book = Book()
            book.title = "Crime #$i"
            book.author = "Author #$i"
            book.isRead = i % 2 == 0
            books += book
        }
    }
}