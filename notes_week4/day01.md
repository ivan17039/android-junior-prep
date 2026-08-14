# Clean Architecture

* **UI sloj** – Ono što se crta i reagira na dodire (`BookListScreen`, `BookCard`, `BookDetailScreen`). Ne zna ništa o mreži ni bazi, samo prikazuje ono što mu je dano.
* **Data sloj** – Dijelovi koji znaju KAKO i ODAKLE stvarno doći do podataka (`ApiService`, `BookDao`, `AppDatabase`), te sirovi podaci (`OpenLibraryBookDto`, `BookEntity`).
* **Domain sloj** – Čist oblik podataka (`Book` data class), neovisan o tome odakle je stigao (s interneta ili iz baze). `BookRepository` sve DTO i Entity objekte pretvara u taj pravi oblik.

> **Napomena:** Puno pravih, pa i profesionalnih projekata (pogotovo manjih) NEMA strogo odvojen domain sloj s posebnim folderom i *use case* klasama — imaju samo UI + Data, s ViewModelom kao mostom između njih. **Bitno je razumjeti zašto se slojevi razdvajaju (jedna odgovornost po sloju).**

---

## Slojevi u ovom projektu

| Datoteka | Sloj |
| --- | --- |
| `BookListScreen.kt`, `BookCard.kt`, `BookDetailScreen.kt`, `AboutScreen.kt` | **UI** |
| `BookListViewModel.kt`, `BookDetailViewModel.kt` | **Most (ViewModel)** |
| `Book.kt` | **Domain (čisti oblik)** |
| `BookRepository.kt` | **Most između domaina i data sloja** |
| `ApiService.kt`, `OpenLibraryBookDto.kt`, `BookDao.kt`, `AppDatabase.kt`, `BookEntity.kt`, `NetworkModule.kt`, `DatabaseModule.kt` | **Data** |

---

## Objašnjenje arhitekture aplikacije

### Kratka verzija (za intervju)

> "Aplikacija koristi MVVM arhitekturu — Jetpack Compose UI sloj prikazuje state iz ViewModela, koji podatke dobiva preko Repository sloja. Repository kombinira Retrofit (mrežni pozivi na Open Library API) i Room (lokalni cache) po principu *single source of truth*, a Hilt povezuje sve slojeve kroz dependency injection."

### Proširena verzija

> "Arhitektura moje aplikacije podijeljena je na slojeve prema MVVM obrascu.
> Koristio sam Jetpack Compose za izradu korisničkog sučelja koje je u potpunosti reaktivno — što znači da se ekran automatski osvježava čim se promijeni stanje u ViewModelu.
> ViewModel ne dohvaća podatke sam, nego komunicira s Repository slojem. Repository je središnje mjesto koje spaja mrežne pozive preko Retrofita i lokalnu Room bazu podataka. Primijenio sam princip *Single Source of Truth* — podaci s mreže prvo se spremaju u Room bazu, a UI uvijek čita iz baze, što aplikaciju čini brzom i omogućuje rad bez interneta.
> Na kraju, sve te dijelove spaja Hilt, koji mi služi za Dependency Injection kako ne bih morao ručno stvarati instancije baza i servisa kroz kod."

---

## Ključni pojmovi

* **MVVM (Model - View - ViewModel):**
* **Model (Data):** Stvarni podaci i logika dohvata.
* **View (UI):** Ekrani; zadatak im je samo nacrtati gumbe, slike i tekst.
* **ViewModel:** "Mozak" ekrana; priprema podatke za View i pamti stanje (preživljava rotaciju ekrana).


* **Jetpack Compose:**
  Googleov alat za izradu UI-ja na Androidu. Sve se gradi preko `@Composable` funkcija. Svaka promjena stanja automatski ponovno iscrtava (*recompose*) samo onaj dio ekrana koji se promijenio.
* **Repository (Sloj skladišta):**
  Posrednik između ViewModela i izvora podataka. Odlučuje odakle ViewModel uzima podatke (s interneta ili iz baze).
* **Single Source of Truth (Jedini izvor istine):**
  Samo je jedan službeni izvor iz kojeg UI smije čitati podatke (lokalna baza u ovoj aplikaciji). Ekran nikada ne prikazuje podatke izravno s interneta, nego čita iz Room baze (Retrofit sprema podatke s interneta u bazu).
* **Retrofit:**
  Android biblioteka koja služi za slanje upita na internet (API) i pretvaranje JSON odgovora u odgovarajući Kotlin objekt.
* **Hilt (Dependency Injection - DI):**
  Alat koji automatski stvara i dostavlja objekte tamo gdje trebaju. Sam prepoznaje što kojem dijelu koda treba i "ubrizgava" (*injecta*) gotov objekt.