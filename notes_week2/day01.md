
# Coroutines osnove

Android aplikacija ima 1 glavni thread (Main/UI thread) zadužen za crtanje ekrana i reagiranje na dodire. Zato spori zadaci (mrežni pozivi, čitanje baze, teški izračuni) zamrznu ekran ili uzrokuju ANR (Application Not Responding).

Rješenje: **Coroutine**
* Kotlin/Java objekt u RAM memoriji 
* Suspendira (pauzira) trenutno stanje i pamti dokle je kod stigao
* Oslobađa thread dok čeka odgovor
* *Lightweight threads* (rade usporedno kao threadovi, ali su dosta jeftinije)

---

## suspend funkcije i delay()

**suspend** - označava funkciju koja može pauzirati izvršavanje i nastaviti kasnije bez blokiranja threada.

```kotlin
// poziv iz coroutine ili druge suspend funkcije
suspend fun fetchData(): String {
    delay(1000) // simulira mrežni poziv koji traje 1 sekundu
    return "Podaci stigli!"
}

```

**delay()** - suspend funkcija koja ne blokira thread (služi za simulaciju sporih operacija).

---

## launch vs async

* **launch** - pokreni pa zaboravi, ne treba rezultat natrag (npr. spremi u bazu, pošalji analitiku, osvježi ekran).

```kotlin
launch {
    val data = fetchData()
    println(data)
}

```

* **async** - pokreni i očekuj rezultat kasnije.
* Vraća `Deferred<T>` na kojem se pozove `.await()` za dobivanje stvarne vrijednosti (pauzira dok ne dođe rezultat).



```kotlin
val deferred = async {
    fetchData()
}
val result = deferred.await()
println(result)

```

---

## Dispatchers

Određuje kojem bloku/grupi threadova pripada coroutine:

* **Dispatchers.Main** - glavni UI thread (za ažuriranje UI-a). NIKAKO ZA SPORE OPERACIJE.
* **Dispatchers.IO** - za input/output operacije koje čekaju neke informacije s mreže, baze, čitanje/pisanje datoteka.
* **Dispatchers.Default** - za CPU-intenzivne zadatke (sortiranje, parsiranje, veliki izračuni).

```kotlin
launch(Dispatchers.IO) {
    val data = fetchData() // mrežni poziv - IO dispatcher
    withContext(Dispatchers.Main) {
        // ažuriranje UI-a - MORA biti na Main
        updateUi(data)
    }
}

```

**withContext()** - način prebacivanja na drugi dispatcher unutar iste coroutine (dohvati podatke na IO pa se vrati na Main da ažuriraš ekran).

---

## Praktični rad: 3 vježbe

```kotlin
package com.ivanb.jobprepapp

import kotlinx.coroutines.*

suspend fun fetchUserName(): String {
    delay(1000)
    return "Ivan"
}

suspend fun fetchUserAge(): Int {
    delay(1000)
    return 23
}

fun main(): Unit = runBlocking {

    // ("Vježba 1.")
    launch {
        val name = fetchUserName()
        println("Korisnik: $name")
    }
    println("Ovo se ispiše ODMAH, prije 'Korisnik: Ivan', jer se launch ne čeka")
    // Ovo se ispiše ODMAH, prije 'Korisnik: Ivan', jer se launch ne čeka
    // Korisnik: Ivan

    // ("Vježba 2.")
    val startTime = System.currentTimeMillis()

    val nameDeferred = async { fetchUserName() }
    val ageDeferred = async { fetchUserAge() }

    val name = nameDeferred.await()
    val age = ageDeferred.await()

    val elapsed = System.currentTimeMillis() - startTime
    println("$name, $age godina. Trajalo: ${elapsed}ms")
    // Ivan, 23 godina. Trajalo: 1149ms

    // ("Vježba 3.")
    launch(Dispatchers.Default) {
        println("Default dispatcher radi na: ${Thread.currentThread().name}")
    }
    launch(Dispatchers.IO) {
        println("IO dispatcher radi na: ${Thread.currentThread().name}")
    }
    // Default dispatcher radi na: DefaultDispatcher-worker-1
    // IO dispatcher radi na: DefaultDispatcher-worker-1

}

```

---

## Dodatne vježbe

1. Napiši `suspend fun fetchWeather(city: String): String` s `delay(800)` koja vraća npr. `"Sunčano u $city-u"`. Pozovi je kroz `launch` za jedan grad i ispiši rezultat.
2. Proširi vježbu 1: dohvati vrijeme za dva grada koristeći `async`, i dokaži (mjerenjem vremena kao u Vježbi 2) da se dohvaćaju usporedno, ne jedno pa drugo.

```kotlin
package com.ivanb.jobprepapp

import kotlinx.coroutines.*

suspend fun fetchWeather(city: String): String {
    delay(800)
    return "Suncano u $city-u."
}

fun main(): Unit = runBlocking {

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

// Suncano u Split-u.
// Suncano u Zagreb-u.
// Trajalo: 943ms

```