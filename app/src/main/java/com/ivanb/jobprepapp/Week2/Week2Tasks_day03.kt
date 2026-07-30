package com.ivanb.jobprepapp.Week2

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// FlowPractice

//1. Napiši fun countingFlow(): Flow<Int> koji emitira brojeve 1 do 5, svaki nakon 300ms čekanja. Collect-aj ga u fun main() i ispiši svaku vrijednost.

//2. Napravi MutableStateFlow<Int>(0), postavi mu vrijednost na 10 (.value = 10) prije nego što ga bilo tko collect-a,
// zatim pokreni collect na njemu u novoj coroutine. Provjeri da prvi ispis odmah pokazuje 10,
// ne 0 — to ti dokazuje da StateFlow novom "slušatelju" odmah da trenutnu vrijednost.


fun countingFlow(): Flow<Int> = flow {
    for (i in 1..5) {
        delay(300)
        emit(i)
    }
}

fun main() = runBlocking {
    println("--- Zadatak 1 ---")
    countingFlow().collect { value ->
        println("Primljeno: $value")
    }

    println("\n--- Zadatak 2 ---")
    val state = MutableStateFlow(0)
    state.value = 10

    val job = launch {
        state.collect { value ->
            println("Trenutna vrijednost: $value")
        }
    }
    delay(100)
    job.cancel()
}