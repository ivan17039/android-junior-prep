# Tjedan 3, Dan 1 – Retrofit setup

## REST API, HTTP metode

- **GET** - čitanje podataka
- **POST** - stvaranje nečeg novog
- **PUT** - ažuriranje postojećeg
- **DELETE** - brisanje

Svi zahtjevi idu na određenu adresu URL/endpoint

## Pretvaranje JSON podataka u Kotlin objekte

Kad se pozove API, server vrati sirov tekst u JSON formatu:

```json
{
  "title": "Dune",
  "author_name": ["Frank Herbert"],
  "first_publish_year": 1965
}
```

Converter (Gson) automatski pretvara tekst u pravi Kotlin objekt tako da usporedi imena JSON polja s imenima svojstava data class

```kotlin
data class Book(val title: String, val author: String, val year: Int)
```

Ako se imena ne poklapaju, kao `author_name` ↔ `authorName`, potrebna je anotacija:

```kotlin
data class OpenLibraryBookDto(
    val title: String,
    @SerializedName("author_name") val authorName: List<String>?,
    @SerializedName("first_publish_year") val firstPublishYear: Int?
)
```

## Open Library za odabir API-ja

```
https://openlibrary.org/search.json?q=dune
```

```json
{
  "numFound": 42,
  "docs": [
    { "title": "Dune", "author_name": ["Frank Herbert"], "first_publish_year": 1965 },
    { "title": "Dune Messiah", "author_name": ["Frank Herbert"], "first_publish_year": 1969 }
  ]
}
```

## Zadaci

**1.** Ručno otvori u browseru `https://openlibrary.org/search.json?q=lord+of+the+rings` (razmak je `+` u URL-u) — pogledaj sirovi JSON, pronađi `docs` polje, pronađi `title` i `author_name` unutar jednog rezultata. Ovo ti pomaže vizualizirati što točno Retrofit "ispod haube" prima i pretvara.

Isječak iz stvarnog odgovora:
```json
"title":"The Lord of the Rings"},{"author_key":["OL26320A","OL14456887A","OL14456888A"],"author_name":["J.R.R. Tolkien","Editorial Editorial World","Robert Robert
```

**2.** Dodaj polje `coverId` u `OpenLibraryBookDto` koje mapira na JSON polje `"cover_i"` (koristi `@SerializedName`) — ovo pripremamo za Dan 4 (Coil, prikaz slika korica).

```kotlin
data class OpenLibraryBookDto(
    val title: String,
    @SerializedName("author_name") val authorName: List<String>?,
    @SerializedName("first_publish_year") val firstPublishYear: Int?,
    @SerializedName("cover_i") val coverId: Int?
)
```

**3.** Razmisli (bez koda): zašto `searchBooks` prima `query: String` kao parametar umjesto da uvijek pretražuje istu, fiksnu riječ? Što to omogućava aplikaciji da radi kasnije?

Parametar služi da funkcija radi i za druge Open Library upite. Kasnije će to omogućiti da se upisani tekst unutar search bara direktno proslijedi kao query.