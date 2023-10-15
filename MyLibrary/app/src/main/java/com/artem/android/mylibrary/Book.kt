package com.artem.android.mylibrary

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Book(@PrimaryKey val id: UUID = UUID.randomUUID(),
                var title: String = "",
                var author: String = "",
                var date: Date = Date(),
                var isRead: Boolean = false,
                var rating: String = "")