# Rukovanje greškama i cache logika

## Tipovi grešaka

* **Nema interneta (`UnknownHostException`):** "Ne znam gdje je taj host" (uređaj nema mrežnu vezu).
* **Timeout (`SocketTimeoutException`):** Veza postoji, ali server ili mreža presporo odgovara.
* **Loš odgovor servera (`HttpException`):** Server odgovori greškom (npr. `404` = ne postoji, `500` = server pokvaren).

> Postoje još deseci rubnih slučajeva, ali hvataju se samo česti, dok ostali imaju generički *fallback*.

---

## `try/catch` vs `runCatching` (Sprečavaju rušenje aplikacije)

### `try/catch`

Klasični pristup — ako unutar `try` bloka dođe do greške, prelazi se u `catch` gdje se obradi greška.

```kotlin
try {
    repository.refreshBooks(query)
} catch (e: Exception) {
    // rukuj greškom
}

```

### `runCatching`

"Uhvati" i rezultat i eventualnu grešku te ih spakira u jedan `Result` objekt koji može biti uspjeh ili neuspjeh.

*(Izvrši A $\rightarrow$ ako uspije, zapakiraj rezultat `Result.success` u kutiju $\rightarrow$ ako pukne, u istu tu kutiju zapakiraj grešku `Result.failure` $\rightarrow$ vrati kutiju za odluku)*

```kotlin
val result = runCatching { repository.refreshBooks(query) }
result.onFailure { e -> /* rukuj greškom */ }
result.onSuccess { /* rukuj uspjehom */ }

```

---

## Zadaci i zabilješke

### Zadatak 1: Testiraj da cache "pobjeđuje" grešku

S upaljenim internetom, pusti app da normalno učita i spremi podatke u Room. Zatim ugasi internet, ali ovaj put NE zatvaraj app potpuno — samo idi na drugi ekran pa se vrati, ili pokreni `refreshBooks()` ručno (npr. povuci-za-refresh ako ga imaš, ili privremeni test-gumb). Provjeri da se ništa ne pokvari — lista ostaje vidljiva, `Error` se NE pojavljuje, jer je `_uiState.value` već `Success`.

* **Rezultat:** Lista knjiga je ostala vidljiva na ekranu, a poruka o grešci se NIJE prikazala.
* **Zaključak:** Provjera `if (_uiState.value !is UiState.Success)` uspješno štiti postojeći cache od pojavljivanja mrežne greške.

---

### Zadatak 2: Razmišljanje bez koda

Što bi se dogodilo (koji bug bi uveo) da si maknuo `if (_uiState.value !is UiState.Success)` provjeru iz `refreshBooks()`? Zamisli scenarij: korisnik ima savršen cache, na kratko izgubi signal, `refreshBooks()` pukne.

* **Odgovor:** Kad bi maknuo tu liniju, `refreshBooks()` bi postavio `UiState.Error` iako Room već ima spremljene podatke od prije. Korisnik bi na kratki prekid signala izgubio cijelu listu s ekrana iako podaci nisu stvarno nestali iz Room-a.