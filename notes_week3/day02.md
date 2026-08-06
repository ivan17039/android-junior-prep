# Tjedan 3, Dan 2 – Repository i Hilt

## Repository, ApiService, Retrofit

**Repository** - brine o izvorima podataka (API, baza, cache) i poslovnoj logici dohvaćanja (kako, kada i odakle se podaci dohvaćaju)

**ApiService** - sučelje u kojem se definira što želimo s interneta

**Retrofit** - vanjska biblioteka koja preuzima ApiService i od njega radi stvarne mrežne pozive. Gradi se preko `.Builder()` ili `.build()`
- otvara mrežnu vezu i šalje upit na internet
- prevodi JSON tekst s interneta u prave Kotlin objekte koje UI može prikazati

## @Provides vs @Inject constructor

Pošto Retrofit nije korisnikova klasa, ne može se dodati anotacija u njenom source kodu, stoga koristimo `@Module` + `@Provides`

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://openlibrary.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
```

- **`@Module`** - označava ovaj objekt kao mjesto gdje Hilt traži funkcije za tipove koje ne može sam konstruirati
- **`@InstallIn(SingletonComponent::class)`** - ove dohvaćene funkcije vrijede za cijelu aplikaciju
- **`@Provides`** - koristi se kad Hilt ne može jednostavno pozvati konstruktor, za sučelja (ApiService nema konstruktor) ili klase iz vanjskih biblioteka čiju izradu ne kontroliraš izravno
- **`@Singleton`** - napravi samo jednu instancu i ponovno je koristi svugdje

**Tvoja vlastita klasa s normalnim konstruktorom** → `@Inject constructor`
**Sučelje, vanjska biblioteka, ili nešto što treba "recept" umjesto jednostavnog konstruktora** → `@Module` + `@Provides`

## Zadaci

**1.** Dokaži compile-time validaciju uživo — privremeno zakomentiraj `provideApiService` funkciju (ili cijeli `NetworkModule`), pokušaj buildati. Pogledaj Hilt grešku koju dobiješ (obično nešto poput "Cannot find binding for ApiService"). Vrati kod natrag. Ovo ti dokazuje ono iz Bloka 3 — Hilt validira cijeli graf ovisnosti pri kompajliranju.

```
error: [Dagger/MissingBinding] com.ivanb.jobprepapp.ApiService cannot be provided without an @Provides-annotated method.
```

**2.** Razmisli (bez koda): zašto `ApiService` ne može imati `@Inject constructor`, dok `BookRepository` može? Napiši dvije rečenice svojim riječima.

ApiService ne može imati @Inject konstruktor jer je to sučelje (interface), a sučelja u Kotlinu nemaju konstruktor niti ih Hilt može izravno instancirati.

S druge strane, BookRepository je naša konkretna klasa s konstruktorom, pa Hilt sam zna kako je stvoriti čim mu dodamo @Inject naljepnicu.