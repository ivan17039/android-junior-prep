// 1. Napiši suspend fun fetchWeather(city: String): String s delay(800) koja vraća npr. "Sunčano u $city-u".
// Pozovi je kroz launch za jedan grad i ispiši rezultat.

// 2. Proširi vježbu 1: dohvati vrijeme za dva grada koristeći async,
// i dokaži (mjerenjem vremena kao u Vježbi 2) da se dohvaćaju usporedno, ne jedno pa drugo.

package com.ivanb.jobprepapp

import kotlinx.coroutines.*
suspend fun fetchWeather(city: String): String {
    delay(800)
    return "Suncano u $city-u."
}
fun main(): Unit = runBlocking{

    val startTime = System.currentTimeMillis()
    val deferred_city1 = async {
        fetchWeather("Split")
    }
    val deferred_city2 = async {
        fetchWeather("Zagreb")
    }

    println(deferred_city1.await())

    println(deferred_city2.await())

    println("Trajalo: ${System.currentTimeMillis() - startTime}ms")

}