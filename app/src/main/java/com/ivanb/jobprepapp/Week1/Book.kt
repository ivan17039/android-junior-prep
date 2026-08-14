package com.ivanb.jobprepapp.Week1

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val year: Int,
    val coverUrl: String? = null
)