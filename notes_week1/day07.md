# Pregled tjedna i odgovori na pitanja

## Dan 1 – Kotlin osnove i null-safety

**1. Koja je razlika između `val` i `var`?**

Val - definiranje varijabli koje se ne mogu izmijenit
Var - mogućnost ponovne izmjene vrijednosti varijable

**2. Što radi Elvis operator `?:`? Napiši primjer.**

Elvis operator koristimo za situacije kod null vrijednosti odnosno kada vrijednost bude null ispisuje se sigurna vrijednost prethodno definirana

Npr.
```kotlin
val name: String? = null
val length = name?.length ?: 0
```

**3. Zašto Kotlin uopće ima null-safety — koji problem rješava?**

Null-safety rjesava problem `NullPointerException` koji je cest uzrok pada aplikacije.

## Dan 2 – Klase, data class, sealed class

**1. Što automatski dobiješ kad koristiš `data class` (usporedi s običnom klasom)?**

Data class koristimo za podatke bez puno logike i s tom opcijom za razliku od normalnih vrijednosti ne vraća mjesto u memoriji gdje se nalazi određeni objekt već same vrijednosti tog objekta. Ovaj tip klase pomaze u pravilnom ispisu stringa, usporedbi, kopiranju objekata radi izmjene odredjenih svojstava te destrukturiranju svojstava.

**2. Zašto bi koristio `sealed class` umjesto obične klase za `UiState`?**

Sealed class koristimo za razlicite vrijednosti podklasa

**3. Daj primjer kad bi koristio `enum class`, a kad `sealed class`.**

Enum za primjer istog objekta kao sto je izmjena stanja semafora dok sealed za opcije kao sto su razlicita stanja u obliku objekta, liste ili string vrijednosti.

## Dan 3 – Prvi Compose ekran

**1. Što je Composable funkcija i po čemu se razlikuje od starog View/XML sustava?**

Composable funkcija omogućava crtanje ekrana dodavanjem razlicitih layoutova.
Razlikuje se od starog sustava jel ne zahtjeva rucno povezivanje elemenata

**2. Čemu služi `Modifier` i kako se lanci pozivaju?**

Modifier služi za uređivanje layoutova na ekranu, a lanci se pozivaju na nacin `Modifier.odredjeni_stil`

**3. Koja je razlika između `Column` i `Row`?**

Column ispisuje elemente jedan ispod drugoga, dok Row jedan pored drugoga.

## Dan 4 – LazyColumn i state

**1. Zašto se za dugačke liste koristi `LazyColumn`, a ne obični `Column` sa scrollanjem?**

Zbog količine podataka koji se ucitava pokretanjem aplikacije, s `LazyColumn`-om ucitavaju se oni trenutno vidljivi dijelovi ekrana te se to azurira scrollanjem.

**2. Što radi `remember` i zašto je potreban — što bi se dogodilo bez njega?**

Remember opcija omogućava da se pri ponovnom pokretanju funkcije stanje ne promijeni nego da ostane spremljeno

**3. Što je "recomposition"?**

Ponovno pozivanje funkcije nakon izmjene podataka

## Dan 5 – Navigacija između ekrana

**1. Kako Navigation Compose zna koji je "trenutni" ekran?**

Pomocu `NavController`-a, `NavHost`-a i Ruta.
`NavController` izdaje naredbu prema određenom ekranu.
`NavHost` prikazuje trenutni ekran preko `composable(route='...')`
Ruta je string koji identificira određeni ekran.

**2. Kako se prosljeđuje argument od liste do detalj-ekrana?**

Argument se stavi u rutu kao placeholder (npr. `book_detail/{bookIndex}`), a kod `navigate()` se ta vrijednost ubaci direktno u string rute. Na drugoj strani, preko `backStackEntry.arguments`, ta vrijednost se izvuce natrag, ali kao string pa ju treba pretvorit u pravi tip kad zatreba (npr. `toIntOrNull()`).

**3. Što bi se dogodilo da pokušaš proslijediti kompleksni objekt umjesto ID-a — zašto se to obično izbjegava?**

Zbog toga sto sitne izmjene kod takvog objekta mogu rezultirat pucanju programa, s ID-jem je jednostavnije.

## Dan 6 – Refaktoring i čišćenje koda

**1. Navedi 2 primjera nečega što si ovaj tjedan napisao lošije nego što bi trebalo, i kako bi to popravio.**

Cijela `MojEkran` funkcija radi previše toga odjednom, popravio bi tako da rasporedim sve funkcije u dane npr. `Day1Screen`, `Day2Screen`.... Ispravio bi to kršenje jedna odgovornost po funkciji.

**2. Zašto je bitno izdvajati ponavljajući UI kod u zasebne Composable funkcije?**

Radi same urednosti i nepotrebnog ponavljanja istih blokova koda.