package Week2

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

    // ("Vjezba 1.")
    launch {
        val name = fetchUserName()
        println("Korisnik: $name")
    }
    println("Ovo se ispiše ODMAH, prije 'Korisnik: Ivan', jer se launch ne čeka")
    // Ovo se ispi�e ODMAH, prije 'Korisnik: Ivan', jer se launch ne ?eka
    // Korisnik: Ivan

    // ("Vjezba 2.")
    val startTime = System.currentTimeMillis()

    val nameDeferred = async { fetchUserName() }
    val ageDeferred = async { fetchUserAge() }

    val name = nameDeferred.await()
    val age = ageDeferred.await()

    val elapsed = System.currentTimeMillis() - startTime
    println("$name, $age godina. Trajalo: ${elapsed}ms")
    // Ivan, 23 godina. Trajalo: 1149ms

    // ("Vjezba 3.")
    launch(Dispatchers.Default) {
        println("Default dispatcher radi na: ${Thread.currentThread().name}")
    }
    launch(Dispatchers.IO) {
        println("IO dispatcher radi na: ${Thread.currentThread().name}")
    }
    // Default dispatcher radi na: DefaultDispatcher-worker-1
    // IO dispatcher radi na: DefaultDispatcher-worker-1

}
