Evo skraćene, čiste verzije za tvoju bilježnicu (**`day06.md`** ili **`NOTES.md`**), točno prema tvojim bilješkama:

---

# Dan 6 – Refaktoring, čišćenje koda i ViewModel

## Bilješke (Notes)

### 1. Imenovanje

Ime varijable ili funkcije mora sama po sebi objasniti što radi ili sadrži.

* **Loše:** `val d = 1965`  ➔  **Dobro:** `val publicationYear = 1965`
* **Loše:** `fun f(b: Book): String`  ➔  **Dobro:** `fun formatBookTitle(book: Book): String`

### 2. Jedna odgovornost po funkciji (Single Responsibility)

Composable ili obična funkcija treba raditi samo jednu stvar.

### 3. Izbjegavanje duplikata (DRY – Don't Repeat Yourself)

Razdvajanje istih ili sličnih blokova koda u zasebnu funkciju ili Composable komponentu.

### 4. ViewModel (Uvod)

Posebna klasa koja omogućava da se zapamti stanje ekrana (state) i nakon rotacije uređaja.

---

## Zadaci za Dan 6

*  Pregled svog koda (Dan 1–5) i primijena ova 3 pravila čistog koda.
*  Napisan kratki `README.md` (opis aplikacije, ekrani, kako pokrenuti).
