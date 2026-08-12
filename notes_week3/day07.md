# Testiranje i Git Workflow

## Vrste testova

* **Unit test:** Komad koda koji automatski provjerava radi li neki drugi komad koda ispravno — bez telefona, bez ekrana i bez klikanja.
* **UI / Instrumentation test:** Test koji stvarno pokreće aplikaciju na pravom ili virtualnom telefonu te klikće po ekranu.

### Lokacije u projektu

* **Unit testovi:** `app/src/test/...`
* **UI testovi:** `app/src/androidTest/...`

---

## JUnit i osnove testiranja

**JUnit** je alat za pisanje testova. Test označićemo s `@Test` iznad obične funkcije.

```kotlin
@Test
fun `dva plus dva je četiri`() { // ime funkcije za testove pišemo unutar ` `
    val rezultat = 2 + 2
    assertEquals(4, rezultat) // provjerava je li rezultat očekivan
}

```

---

## Testiranje coroutine-a

Kada kôd koji testiramo koristi `viewModelScope.launch { }`, potreban nam je poseban testni alat `runTest { }`:

```kotlin
@Test
fun `primjer s coroutine`() = runTest {
    // kôd koji koristi suspend funkcije ide ovdje
}

```

---

## MockK – Mockanje Repository-ja

Testiranje koda koji sadrži pravi `Repository` (koji zove pravu mrežu i pravi Room) je zahtjevno i sporo. Zato uvodimo **lažni (mock) Repository** kojemu ručno kažemo što da vrati.

```kotlin
val mockRepository = mockk<BookRepository>() // napravi lažni BookRepository

// Za obične funkcije/svojstva: "kad netko pozove ovo, vrati ovo"
every { mockRepository.books } returns flowOf(listOf(testBook)) 

// coEvery služi za suspend funkcije; any() znači "bez obzira koji parametar netko proslijedi"
coEvery { mockRepository.refreshBooks(any()) } returns Unit 

// Za testiranje greške umjesto returns stavljamo throws
coEvery { mockRepository.refreshBooks(any()) } throws UnknownHostException()

```

---

## Git: `merge` vs `rebase`

*`main` grana u svakom trenutku mora biti u ispravnom i radnom stanju.*

* **`git merge`:** Spaja novu granu (koja se trenutno gradi s novim *commitovima*) s glavnom `main` granom stvarajući jedan zajednički "merge commit".
* **`git rebase`:** Premješta *commitove* tako da izgleda kao da su od početka pisani izravno na `main` grani.

---

## Zadaci i praktični rad

### 1. Testiranje cache zaštite

*Zadatak: Dodaj treći test — "kad `refreshBooks` baci grešku ALI je stanje već `Success` (cache postoji), stanje ostaje `Success` i ne postaje `Error`".*

```kotlin
@Test
fun `kad refreshBooks baci grešku ALI je stanje već Success (cache postoji), stanje ostaje Success, ne postane Error`() = runTest {
    val testBooks = listOf(Book("Dune", "Frank Herbert", 1965, null))
    val mockRepository = mockk<BookRepository>()
    every { mockRepository.books } returns flowOf(testBooks)
    coEvery { mockRepository.refreshBooks(any()) } throws UnknownHostException()

    val viewModel = BookListViewModel(mockRepository)
    advanceUntilIdle()

    assertEquals(UiState.Success(testBooks), viewModel.uiState.value)
}

```

---

### 2. Namjerno rušenje testa

*Zadatak: Pokvari jedan test (npr. `assertEquals(5, rezultat)` umjesto 4) i pogledaj ispis.*

**Ispis greške iz konzole:**

```text
Expected :5
Actual   :4

java.lang.AssertionError: expected:<5> but was:<4>
    at com.ivanb.jobprepapp.BookListViewModelTest.dva plus dva je četiri(BookListViewModelTest.kt:80)

BookListViewModelTest > dva plus dva je četiri FAILED
    java.lang.AssertionError at BookListViewModelTest.kt:80

4 tests completed, 1 failed
> Task :app:testDebugUnitTest FAILED
BUILD FAILED in 9s

```

---

### 3. Namjerni Merge Conflict

*Zadatak: Izmijeni isti redak koda drugačije na `main` i na svojoj grani te ih pokusaj spojiti.*

**Pokretanje naredbe u terminalu:**

```bash
PS C:\Users\Ivan\AndroidStudioProjects\JobPrepApp> git merge feature/sort-books
Auto-merging app/src/main/java/com/ivanb/jobprepapp/Week2/BookListViewModel.kt
CONFLICT (content): Merge conflict in app/src/main/java/com/ivanb/jobprepapp/Week2/BookListViewModel.kt
Automatic merge failed; fix conflicts and then commit the result.

```

**Izgled koda s Git oznakama konflikta u datoteci:**

```kotlin
<<<<<<< HEAD
                    // Promjena na main grani:
                    _uiState.value = UiState.Success(books.sortedBy { it.title.uppercase() })
=======
                    // Promjena na feature grani:
                    _uiState.value = UiState.Success(books.sortedBy { it.title.lowercase() })
>>>>>>> feature/sort-books

```