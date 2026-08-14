package com.ivanb.jobprepapp.Week3

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    // FIX: Removed autoGenerate = true because SQLite only supports it for Int/Long primary keys.
    // Since we use the OpenLibrary 'key' (String) as our ID, we must provide it manually.
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val year: Int,
    val coverUrl: String?
)