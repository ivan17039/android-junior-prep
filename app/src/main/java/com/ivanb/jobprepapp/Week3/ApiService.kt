package com.ivanb.jobprepapp

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

// Opisuje što postoji
interface ApiService {
    @Headers("User-Agent: JobPrepApp/1.0 (tvoj-email@example.com)")
    @GET("search.json") // Retrofit šalje HTTP GET na [baseUrl]/search.json
    suspend fun searchBooks(@Query("q") query: String): SearchResponse
    // parametar funkcije postaje URL query parametar i tako pozivom searchBooks("dune") pogađamo .../search.json?q=dune
    // Povratni tip SearchResponse — Retrofit automatski pretvori JSON odgovor u ovaj objekt, potpuno bez ručnog parsing koda
}