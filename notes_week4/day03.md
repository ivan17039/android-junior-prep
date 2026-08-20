# Testiranje i Arhitektura Bilješke

**Test coverage** — koliki postotak koda testovi stvarno izvrše dok se pokreću.

* Ako kod poziva funkcija ne provjeri ništa smisleno (`assert` / potvrda), pokrivenost ne znači puno.

**Bolji pristup je:** *"Koji dio koda bi me najviše iznenadio da tiho pukne?"*

Tako imamo:

* **Logika koja se grana (`if/else`, `when`):** više putanja znači više prilika za grešku.
* **Logika gdje bug ne bi glasno puknuo (`crash`):** pogrešni podaci ili zaglavljenost u stanju.
* **Kod koji mapira podatke (`DTO -> Entity`, `Entity -> Book`):** tipfeler samo tiho izgubi ili iskrivi podatak, ne baci grešku.

> **Napomena:**
> Compose layout je bolje vizualno provjeriti umjesto testirati.
> Kod bez grananja koji samo proslijeđuje podatke nije vrijedno testirati.

---

## Potrebno uvesti testiranje Repository-ja

* **Mapiranje podataka:** Repository uzima sirove podatke s mreže (`DTO`) i pretvara ih u objekte za bazu (`Entity`) ili za UI (`Book`). Ako se u tom mapiranju zaboravi jedno polje ili se krivo pridruže vrijednosti, aplikacija se neće srušiti — ali će prikazati krive ili prazne podatke.
* **Izvor podataka:** Repository odlučuje hoće li uzeti podatke iz Room baze ili s API-ja, te kada treba osvježiti spremljene podatke. Tu ima puno `if/else` grananja koja je lako previdjeti bez testa.

---

## Ključni pojmovi u MockK testiranju

* **`relaxUnitFun = true`**
  Omogućuje da sve funkcije koje vraćaju `Unit` (nikakvu vrijednost) uspiju bez da se za svaku piše posebno `coEvery { ... } returns Unit`.
* **`every { mockDao.getAll() } returns flowOf(emptyList())`**
  Uvodimo kako test ne bi pukao jer se `BookRepository`-jev `books` `Flow` property inicijalizira odmah čim se napravi instanca:
  `val books = bookDao.getAll().map { ... }`
  Čim se pozove `BookRepository(mockApiService, mockDao)`, `mockDao.getAll()` je već pozvan.
* **`coVerify { mockDao.insertAll(listOf(expectedEntity)) }`**
  Provjerava: *"Je li se ova suspend funkcija stvarno pozvala, s točno ovim argumentom, nakon što se testirani kod izvršio?"*

---

## Zadaci

### 1. Testiranje propagacije mrežne greške

Napiši još jedan Repository test: što se dogodi ako `apiService.searchBooks(...)` baci grešku (npr. `UnknownHostException`) — propagira li `refreshBooks()` tu grešku dalje umjesto da je tiho proguta?

*(Podsjetnik: Repository namjerno NE hvata greške sam — to je posao ViewModela).*

```kotlin
@Test
fun `refreshBooks propagira mrežnu grešku umjesto da je proguta`() = runTest {
    val mockApiService = mockk<ApiService>()
    val mockDao = mockk<BookDao>(relaxUnitFun = true)
    coEvery { mockApiService.searchBooks(any()) } throws UnknownHostException()
    every { mockDao.getAll() } returns flowOf(emptyList())

    val repository = BookRepository(mockApiService, mockDao)

    var thrown = false
    try {
        repository.refreshBooks("dune")
    } catch (e: UnknownHostException) {
        thrown = true
    }
    assertTrue(thrown)
}

```

---

### 2. Razmišljanje o rubnim slučajevima (Edge-cases)

Sad kad znaš da je "prazna lista" imala prikriveni bug — koji bi drugi scenarij u tvojoj app mogao imati sličan problem, gdje kod tehnički radi, ali specifičan edge-case nikad nije eksplicitno testiran ni ručno isproban?

* **Ekstremno dugački tekstovi:** Pregazi li tekst gumb pored sebe, ispadne li iz ekrana ili se uredno skrati s tri točke?
* **Knjige bez naslovnice (`coverUrl = null`):** OpenLibrary API vrati knjigu koja nema sliku (`null`), autora (`null`) ili je godina izdanja `0` $\rightarrow$ prikaže li placeholder (sivu zamjensku sliku)?
* **Mrežna greška za offline cache:** Korisnik ima 10 spremljenih knjiga u bazi, ugasi Wi-Fi i napravi *pull-to-refresh* $\rightarrow$ pokazuje li aplikacija i dalje stare knjige uz obavijest da su trenutno prikazani cache podaci ili prebriše sve i baci ružan Error?