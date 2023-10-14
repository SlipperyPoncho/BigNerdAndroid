package com.artem.android.mylibrary

import android.app.Application

class MyLibraryApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        BookRepository.initialize(this)
    }
}