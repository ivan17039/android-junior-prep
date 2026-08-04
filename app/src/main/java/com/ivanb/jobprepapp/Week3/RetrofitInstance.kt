package com.ivanb.jobprepapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: ApiService = Retrofit.Builder()
        .baseUrl("https://openlibrary.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java) // Retrofit pogleda tvoj interface i anotacije, i sam generira pravi, radni kod koji stvarno šalje HTTP zahtjeve.
}