---

## Dan 1 – Coroutines osnove

### 1. Zašto se coroutines opisuju kao "lightweight threads"?

Zato što rade usporedno kao dretve (threads), ali su **dosta jeftinije po resursima** jer ne zahtijevaju svoju vlastitu OS dretvu. Umjesto toga omogucuju **suspend** funkcije kojima se pauzirana stanja mogu nastaviti bez blokiranja sistemskih dretvi.

### 2. Kad bi koristio `async` umjesto `launch`?

`async` koristimo kada nam je potreban **povratni rezultat** (preko `Deferred.await()`) te u situacijama kada želimo izvršiti više asinhronih zadataka paralelno i prikupiti njihove rezultate.

### 3. Zašto mrežni poziv ne smije ići na `Dispatchers.Main`?

`Dispatchers.Main` je namijenjen za glavni **UI thread**. Zadužen je za crtanje ekrana i reagiranje na korisničke dodire tijekom rada aplikacije. Izvršavanje mrežnih poziva na njemu usporilo bi i zamrznulo aplikaciju (*Application Not Responding*).

---

## Dan 2 – ViewModel i viewModelScope

### 1. Što se događa s običnim state-om u Composable-u kod rotacije ekrana, a što s onim u ViewModelu?

Obični state u Composable-u **gubi spremljene podatke** kada se `Activity` uništi i ponovno stvori zbog rotacije ekrana (osim ako se ne koristi `rememberSaveable`). S druge strane, podaci u `ViewModel`-u se spremaju u `ViewModelStore` objekt i uspješno **preživljavaju rotaciju**.

### 2. Zašto se coroutine u ViewModelu pokreće kroz `viewModelScope`, a me kroz obični `GlobalScope`?

Zbog toga što je životni vijek coroutine-a u potpunosti **povezan sa životnim vijekom ViewModela**. Kada se ViewModel uništi (npr. zatvaranjem ekrana), `viewModelScope` automatski otkazuje sve aktivne coroutine-e i tako sprečava curenje memorije (*memory leaks*) i nepotrebno trošenje resursa.

---

## Dan 3 – Flow i StateFlow

### 1. Zašto `StateFlow` uvijek ima "trenutnu vrijednost", a obični `Flow` ne mora?

`StateFlow` je po svojoj prirodi **držač stanja (state holder)** i namijenjen je za prikaz trenutnog stanja na UI-ju, zbog čega u svakom trenutku mora imati zadanu vrijednost. Obični `Flow` je hladni tok podataka (*cold stream*) koji emitira vrijednosti samo dok se aktivno prikuplja (`collect`), bez obveze pamćenja zadnjeg stanja.

### 2. Što radi `collectAsState()` u Composable funkciji?

Stalno osluškuje promjene u `StateFlow`-u (ili `Flow`-u) iz ViewModela te ih pretvara u Compose `State`. Na taj način omogućuje automatski **recomposition** (precrtavanje ekrana) čim stignu novi podaci.

---

## Dan 4 – UiState pattern u praksi

### 1. Zašto je korisno imati eksplicitno Loading/Error/Success stanje umjesto samo prikazivanja liste čim stigne?

Kako bismo na jasan i siguran način upravljali korisničkim sučeljem ovisno o trenutnom statusu podataka. Pomoću eksplicitnih stanja UI točno zna treba li prikazati indikator učitavanja (*spinner*), prazan ekran, poruku o grešci ili sam sadržaj (listu).

### 2. Gdje bi u realnoj aplikaciji vjerojatno nastala greška (Error stanje)?

Najčešće pri mrežnim pozivima (gubitak internetske veze, *timeout*, nedostupan poslužitelj s greškom `500`), greškama pri parsiranju podataka ili neuspješnom čitanju/pisanju u lokalnu bazu podataka.

---

## Dan 5 – Navigation Compose s argumentima

### 1. Zašto je dobra praksa da svaki ekran ima svoj ViewModel, umjesto jednog velikog "God ViewModel-a" za cijelu app?

Prvenstveno zbog **razdvajanja odgovornosti (*Single Responsibility Principle*)** i usklađenosti sa životnim vijekom samog ekrana. Jedan veliki "God ViewModel" postao bi nepregledan za održavanje, a podaci bi nepotrebno ostajali u memoriji i nakon što korisnik napusti pojedini ekran.

---

## Dan 6 – Hilt osnove (Dependency Injection)

### 1. Što bi morao raditi ručno kad ne bi imao Hilt (kako bi inače ViewModel dobio svoje ovisnosti)?

Morao bih sam ručno napisati `ViewModelProvider.Factory` klasu koja zna kako stvoriti instancu ViewModela s njegovim ovisnostima. Tu bih tvornicu morao ručno pozivati na svakom ekranu i ažurirati je pri svakom dodavanju nove ovisnosti. Uz Hilt (DI), klasa samo zatraži potrebne parametre kroz `@Inject` konstruktor, a Hilt se u pozadini pobrine za njihovo stvaranje i ubrizgavanje.

### 2. Što rade anotacije `@HiltAndroidApp` i `@AndroidEntryPoint`?

* **`@HiltAndroidApp`**: Postavlja se iznad vlastite `Application` klase. Pokreće Hiltovo generiranje koda i stvara glavni dependency injection kontejner na razini cijele aplikacije.
* **`@AndroidEntryPoint`**: Postavlja se iznad Android komponenti (poput `MainActivity` ili `Fragmenta`) i označava ih kao ulazne točke u koje Hilt može ubrizgavati ovisnosti.