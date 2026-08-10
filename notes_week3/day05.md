# Lokalni cache i Single Source of Truth (SSOT)

Do sada, svaki put kad `ViewModel` zatraži podatke, `Repository` poziva `ApiService` i čeka mrežni odgovor.

**Problemi takvog pristupa:**

1. Ako nema interneta, nema podataka uopće.
2. Svaki put se iznova čekaju mrežni odgovori, čak i za iste podatke koji su dohvaćeni od prije.

**Rješenje (SSOT — Single Source of Truth):**

* **Repository** dohvaca podatke s mreže u pozadini i sprema ih u lokalnu bazu. Nakon toga čita podatke iz baze i daje ih `ViewModel`-u.
* **ViewModel** te podatke prosljeđuje UI-ju.
* **UI** sada ne pita bazu niti mrežu izravno, već komunicira s `ViewModel`-om za prikaz podataka.

**Ovo je bitno za:**

* **Offline pristup:** Korisnik vidi prošle dohvaćene podatke umjesto praznog ekrana.
* **Brže percipirano učitavanje:** Ako baza već ima podatke, prikažu se odmah dok se osvježavanje (*refresh*) obavlja u pozadini.
* **Manje mrežnih poziva:** Svi dijelovi aplikacije koji trebaju iste podatke čitaju iz baze umjesto da svaki šalje zaseban mrežni upit.

---

## Gradivni dijelovi Room baze

Room ima tri glavna gradivna dijela: `@Entity`, `@Dao` i `@Database`.

### 1. `@Entity`

Obična `data class` koja opisuje **jedan red** u tablici baze. Svako polje postaje stupac.

```kotlin
@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Obavezan jedinstveni broj za svaki red
    val title: String,
    val author: String,
    val year: Int,
    val coverUrl: String?
)

```

### 2. `@Dao` (Data Access Object)

`Interface` koji opisuje **što se sve može raditi** s tom tablicom.

```kotlin
@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAll(): Flow<List<BookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(books: List<BookEntity>)

    @Query("DELETE FROM books")
    suspend fun deleteAll()
}

```

### 3. `@Database`

Spaja `Entity` klase i `DAO` sučelja u jedan objekt koji predstavlja stvarnu bazu:

```kotlin
@Database(entities = [BookEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}

```

> **Room + Flow:** Čim se podaci u bazi izmijene (`deleteAll` / `insertAll`), uz pomoć `Flow` mehanizma (kojim se stalno gledaju promjene) nastoji se prikazati što ažurniji ekran s pravim podacima.

---

## Zadaci i zabilješke

### Zadatak 1: Compile-time SQL validacija

Dokaži compile-time SQL validaciju uživo — privremeno promijeni `"SELECT * FROM books"` u nešto pogrešno, npr. `"SELECT * FROM bookz"`, pa pokušaj buildati. Pogledaj Roomovu grešku.

* **Upit:** `"SELECT * FROM bookz"`
* **Ispis greške u Logcatu:**
```text
[ksp] There is a problem with the query: [SQLITE_ERROR] SQL error or missing database (no such table: bookz)

```



---

### Zadatak 2: Dokaz da je Room na disku

Dokaži da je Room stvarno na disku, a ne u memoriji — pokreni app s internetom, potpuno zatvori app, ugasi internet i ponovno otvori.

* **Opažanje:** Naslovi i autori se prikazuju, ali slike se ne prikazuju jer je u bazi spremljen samo link (URL) do slike, koji zahtijeva mrežnu vezu za preuzimanje.

---

### Zadatak 3: Brisanje podataka bez mreže

Obriši app podatke (*Postavke → Aplikacije → tvoja app → Pohrana → Clear data*) **DOK je internet ugašen**, pa pokreni aplikaciju.

* **Opažanje:** Nakon brisanja lokalnih podataka i pokretanja aplikacije bez interneta, mrežni poziv vraća grešku:
```text
Unable to resolve host "openlibrary.org": No address associated with hostname

```



---

### Zadatak 4: Razmišljanje (Flow vs Suspend)

Zašto `bookDao.getAll()` vraća `Flow<List<BookEntity>>` umjesto obične `suspend fun getAll(): List<BookEntity>`? Što bi se izgubilo da je obični jednokratni `suspend` poziv?

* **Odgovor:** Vraća `Flow` jer on automatski emitira novu listu i UI se automatski osvježi nakon što se tablica promijeni. Ako bi bila `suspend` funkcija (odnosno tip koji vrati listu samo jednom), morali bismo ručno ponovno pozivati funkciju svaki put kad se promjene podaci u bazi.