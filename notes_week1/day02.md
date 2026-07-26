# Dan 2 – Klase, data class, sealed class

## Klase i konstruktori

Osnovna klasa u Kotlinu:

```kotlin
class Person(val name: String, var age: Int)
```

Ova linija istovremeno:
- stvori klasu Person
- definira konstruktor (Primary Constructor) *name* i *age*
- spremi podatke unutar klase (svojstva/properties) *var* i *val*

Tako imamo primjer:

```kotlin
val person = Person("Ana", 25)
println(person.name)   // Ana
person.age = 26         // OK jer je age var
```

init blok - kod koji se odmah izvrsi prilikom stvaranja objekta (npr. validacija)

```kotlin
class Person(val name: String, var age: Int) {
    init {
        require(age >= 0) { "Age cannot be negative" }
    }
}
```

metode unutar klase

```kotlin
class Person(val name: String, var age: Int) {
    fun greet(): String = "Hi, I'm $name"
}
```

## data class

- za situacije kad klasa sadrzi samo podatke bez puno logike
- normalne klase vrate memoriju na kojoj se nalazi ta klasa npr. Book@2c7b84de

```kotlin
data class Book(val title: String, val author: String, val year: Int)
```

**toString()**

```kotlin
val book = Book("Dune", "Frank Herbert", 1965)
println(book)   // Book(title=Dune, author=Frank Herbert, year=1965)
```

**equals() / hashCode()**

```kotlin
val book2 = Book("Dune", "Frank Herbert", 1965)
println(book == book2)   // true (kod obične class bilo bi false jel se nalazi na razlicitim adresama)
```

**copy()**

```kotlin
val newerEdition = book.copy(year = 2021)
println(newerEdition)   // Book(title=Dune, author=Frank Herbert, year=2021)
```

**Destrukturiranje**

```kotlin
val (title, author, year) = book // za ona svojstva koja nam ne trebaju - val (title, _, year) = book

println("$title by $author ($year)")
```

## sealed class vs enum class

*Enum class
- za one situacije kada se unaprijed znaju sve moguce opcije

```kotlin
enum class TrafficLight { RED, YELLOW, GREEN }
fun action(light: TrafficLight) = when(light){

   TrafficLight.RED -> "Stop"
   TrafficLight.YELLOW -> "Slow Down"
   TrafficLight.GREEN -> "Go"   

}
```

- dodatno Enum vrijednosti mogu imati i svoja svojstva s tim da svaka instanca istog enuma ima isti oblik podataka

```kotlin
enum class TrafficLight(val durationSeconds: Int, val hexColor: String) {
    RED(30, "#FF0000"),
    YELLOW(5, "#FFFF00"),
    GREEN(45, "#00FF00")
}
```

*sealed class
- ograničena hijearhijska klasa gdje svaka podklasa moze nositi razlicite podatke

```kotlin
sealed class UiState{
   object Loading: UiState()
   data class Success(val data: List<Book>): UiState()
   data class Error(val message: String): Uistate()
}

fun render(state: Uistate) = when(state) {
   is UiState.Loading -> "Loading..."
   is UiState.Success -> "Got ${state.data.size} books"
   is UiState.Error -> "Error: ${state.message}"
```

exhaustive when = when izraz pokriva sve moguće slučajeve koji mogu postojati u kodu

## Primjeri

**1-3 data class, equals, copy, destrukturiranje**

```kotlin
data class Movie(val title: String, val director: String, val rating: Double)
fun main() {

    val movie = Movie("Inception", "Christopher Nolan", 8.8)
    val movie1 = Movie("Inception", "Christopher Nolan", 8.8)
	println(movie == movie1)
   	println(movie)
    
    val movie2 = movie.copy(rating = 8.5)
    println(movie2)
    
    
    println("The movie titled '${movie.title}' was directed by ${movie.director} and received a rating of ${movie.rating}.") // Prije
    
    val (title, director, rating) = movie
    println("The movie titled '$title' was directed by $director and received a rating of $rating.") // Koristenjem destrukcije
   		
}
```

**4. enum class**

```kotlin
enum class TrafficLight {RED, YELLOW, GREEN}

fun action(light: TrafficLight) = when(light){
        TrafficLight.RED -> "Stop"
        TrafficLight.YELLOW -> "Slow Down"
        TrafficLight.GREEN -> "Go"

}

//POZIV
val light = action(TrafficLight.GREEN)
println(light)
```

**5. sealed class + when**

```kotlin
sealed class Result{
    
    data class Success(val value: Int) : Result()
    data class Failure(val error: String) : Result()
    
}
fun handle(result: Result) = when(result){
    is Result.Success -> "Got value: ${result.value}"
    is Result.Failure -> "Failure ${result.error}"
}

//POZIV
val uspjesanRezultat: Result = Result.Success(42)
val neuspjesanRezultat: Result = Result.Failure("Greška 404!")
    
println(uspjesanRezultat)
println(neuspjesanRezultat)
```

**6. Kombinirano – Book + UiState**

```kotlin
data class Book(val title: String, val author: String, val year: Int)

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<Book>) : UiState()
    data class Error(val message: String) : UiState()
}

fun render(state: UiState) = when (state) {
    is UiState.Loading -> "Loading..."
    is UiState.Success -> "Got ${state.data.size} books"
    is UiState.Error -> "Error: ${state.message}"
}

//POZIV
println(render(UiState.Loading))
println(render(UiState.Success(listOf(Book("Dune", "Frank Herbert", 1965)))))
println(render(UiState.Error("Network failure"))
```