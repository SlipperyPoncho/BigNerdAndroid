package com.artem.android.mylibrary

import java.util.Date
import java.util.UUID

data class Book(val id: UUID = UUID.randomUUID(),
                var title: String = "",
                var author: String = "",
                var date: Date = Date(),
                var isRead: Boolean = false)