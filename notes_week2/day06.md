# Tjedan 2, Dan 6 – Dependency Injection

## Loš pristup

```kotlin
class BookListViewModel : ViewModel() {
    private val repository = BookRepository() // sam stvara svoju ovisnost
}
```

## Rješenje

Dependency Injection (DI) rjesava tako da klasa zatrazi sto joj treba u parametru konstruktora a netko drugi je odgovoran da pribavi podatke

```kotlin
class BookListViewModel(
    private val repository: BookRepository // netko izvana ovo daje
) : ViewModel()
```

## Hilt anotacije

Ti se podaci pribave preko Hilt anotacija kao što su:

- **`@HiltAndroidApp`** - iznad Application klase (cijela aplikacija u pozadini)
- **`@AndroidEntryPoint`** - iznad Activity odnosno MainActivity klase (prvi glavni ekran)
- **`@HiltViewModel`** - ide na ViewModel klasu s kojom upravlja (dohvaca se preko `hiltViewModel()`)
- **`@Inject constructor(...)`** - ide na konstruktor iste ViewModel klase (koje ovisnosti, parametre treba)

## Primjer

"Upravljaj ovim ViewModelom!"

```kotlin
@HiltViewModel
class BookListViewModel @Inject constructor( // 🟢 2. Naljepnica ispred konstruktora govori: "Ubaci ovdje što treba!"
    private val repository: BookRepository
) : ViewModel() {
    // ViewModel kod...
}
```