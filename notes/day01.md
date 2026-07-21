# Dan 1 – Kotlin osnove, null-safety

## Kotlin osnove – teorija

**val vs var**
- `val` – ne može se mijenjati
- `var` – može se mijenjati

**Funkcije**

```kotlin
fun sum(a: Int, b: Int): Int {
    return a + b
}

// kraća verzija
fun sum(a: Int, b: Int) = a + b
```

**if / when kao izraz**

```kotlin
// if
val biggerNumber = if (a > b) a else b

// when
val grade = when {
    score >= 90 -> "A"
    score >= 80 -> "B"
    else -> "F"
}
```

## Null-safety – teorija

- `NullPointerException` – čest uzrok pada aplikacije
- Kotlin te tjera da eksplicitno odrediš može li vrijednost biti null

```kotlin
var nickname: String? = null   // može biti null
```

**Safe call `?.`** – pristup vrijednosti samo ako nije null, inače vrati null

```kotlin
val length = nickname?.length   // Int? – ili broj ili null
```

**Elvis operator `?:`** – ako je null, koristi ovo umjesto

```kotlin
val length = nickname?.length ?: 0
```

**Not-null assertion `!!`** – siguran sam da nije null, pukne ako jest

```kotlin
val length = nickname!!.length   // baca NullPointerException ako JE null – izbjegavaj u pravom kodu!
```

**`let`** – izvrši blok samo ako vrijednost nije null (jamči da unutar bloka nema šanse da je vrijednost null)

```kotlin
nickname?.let { sigurniNadimak ->
    println("Nickname: $sigurniNadimak")
    println("Duljina nadimka je: ${sigurniNadimak.length}")
}

// kraća verzija s 'it'
nickname?.let {
    println("Nickname: $it")
}
```

**Kombinirano s Elvis operatorom**

```kotlin
nickname?.let {
    println("Pronađen nadimak: $it")
} ?: println("Korisnik uopće nema nadimak.")

// Ako je nickname = null, cijeli blok unutar { } se preskače.
// Program ide dalje na iduću liniju koda kao da se ništa nije dogodilo – bez rušenja, bez grešaka.
```

---

## Primjeri (moji riješeni zadaci)

```kotlin
fun main() {
    zad1()
    println("Između 5 i 3 veći je ${biggerNumber(5, 3)}")
    println("Ocjena za postignutih 89 bodova je ${grade(89)}")

    var nickname: String? = "Marko"
    val length = nickname?.length ?: 0
    println(length)

    printUppercaseIfPresent("kotlin")
    printUppercaseIfPresent(null)

    val user2: String? = null
    println(user2?.length ?: 0)

    val user1: String? = null
    println(user1!!.length)
}

fun zad1() {
    val name = "Ivan"
    var kotlinLevel: Int = 3

    println("Bok ja sam $name i moj kotlin level je $kotlinLevel")
    kotlinLevel = 5
    println("Bok ja sam $name i moj kotlin level je $kotlinLevel")
}

fun biggerNumber(a: Int, b: Int): Int = if (a > b) a else b

fun grade(score: Int): String {
    return when {
        score >= 90 -> "A"
        score >= 80 -> "B"
        score >= 70 -> "C"
        score >= 60 -> "D"
        else -> "F"
    }
}

fun printUppercaseIfPresent(value: String?) {
    value?.let {
        println(it.uppercase())
    }
}
```