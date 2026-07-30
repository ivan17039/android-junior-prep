# Tjedan 2, Dan 3 – Flow i StateFlow

## Flow

Flow - Kotlinov nacin da se prikaze niz (stream) vrijednosti kroz vrijeme umjesto jedne vrijednosti (npr. cijene dionice koja se osvjezava svake sekunde)

```
Suspend funkcija:  poziv --> [čekanje] --> JEDNA vrijednost, gotovo

Flow:              poziv --> [čekanje] --> vrijednost 1
                          --> [čekanje] --> vrijednost 2
                          --> [čekanje] --> vrijednost 3
                          --> ... (može trajati dokle god treba)
```

Npr. flow predstavlja mjesecna pretplata koja se kroz odredjeno vrijeme naplacuje dok se ne otkaze

## flow{}, emit(), collect()

```kotlin
fun countingFlow(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(500)
        emit(i)
    }
}
```

flow - opisuje kako ce se podaci generirati
emit - trenutak kada je podatak spreman za slanje na traku (stream)

collect{} - suspend funkcija koja ceka svaku sljedeću vrijednost i izvrsi blok koda za nju sve dok flow ne zavrsi

```kotlin
fun main() = runBlocking {
    countingFlow().collect { value ->
        println("Primljeno: $value")
    }
}
```

Kod unutar flow-a (hladan - ne izvrsava se) sve dok netko ne collect-a

## StateFlow

StateFlow - poseban tip Flowa koji je uveden jel Flow ne pamti zadnju vrijednost, los je sa stanjima (state) stoga ako pocne collect-ati nakon emitiranja propustit ce odredjena stanja

StateFlow:
- uvijek ima trenutnu vrijednost i potrebno je inicijalizirati pocetnu (zadnja poslana dostupna preko .value)
- novi slusatelj collector odmah dobije trenutnu vrijednost pa tek buduce promjene stoga nista ne propusta

```kotlin
val stateFlow = MutableStateFlow("početna vrijednost")
println(stateFlow.value) // čitaš odmah, sinkrono, bez collect-a
```

## Private/public

```kotlin
private val _books = MutableStateFlow<List<Book>>(emptyList())
val books: StateFlow<List<Book>> = _books.asStateFlow()
```

_books (privatno, Mutable): "Samo u ViewModelu mijenjanje podataka."
books (javno, Read-only): "Svi mogu gledati trenutno stanje, ali ne mogu manipulirati sa strane."

```kotlin
// 🔴 OVO OVDJE JE VIEWMODEL:
// To je obična Kotlin klasa s imenom (npr. BookListViewModel)
// koja na kraju ima zapisano ": ViewModel()"
class BookListViewModel : ViewModel() {

    // 🔒 1. "Sef" unutar ViewModela (PRIVATNO)
    // Budući da piše "private", ove varijable nema nigdje izvan ove zagrade {...}
    private val _books = MutableStateFlow<List<Book>>(emptyList())

    // 👁️ 2. "Ekran" izvan ViewModela (JAVNO)
    // Ovo je dostupno Compose ekranu za čitanje
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    // ⚙️ 3. Funkcija unutar ViewModela koja JEDINA smije mijenjati podatke
    fun učitajKnjige() {
        // Samo OVDJE unutar klase smijemo pisati u _books.value!
        _books.value = listOf(
            Book("Kotlin Vodič", "Nepoznat autor", 2024),
            Book("Android Za Početnike", "Nepoznat autor", 2024)
        )
    }
} // 👈 Ovdje završava ViewModel
```

## collectAsState()

```kotlin
val books by viewModel.books.collectAsState()
```

On sjedi na ekranu, drži "uho" prislonjeno na viewModel.books, i čim se unutar ViewModela promijeni podatak u _books.value, collectAsState() to odmah primijeti, automatski pokrene recomposition i nacrta nove knjige na mobitelu

Svaki put kad Flow emitira novu vrijednost, ažurira Compose State — što pokreće recomposition

---

## Zadaci 1–2

```kotlin
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
```

## Zadatak 3

`BookListScreen.kt`
```kotlin
......
item{
    Button(onClick = {viewModel.clearBooks()}, modifier = Modifier.padding(16.dp)){
        Text("Reset Books")
    }
}
```

`BookListViewModel`
```kotlin
fun clearBooks(){
    _books.value = emptyList()
}
```