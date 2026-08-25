# Tjedan 4, Dan 6 – Detaljni plan: priprema priče o projektu i tehničkih odgovora

Najbolji odgovor spaja točnu definiciju s konkretnim primjerom iz nečega što se stvarno napravilo.

---

## Priča o projektu

Ovo je aplikacija za pretraživanje knjiga koju sam izradio tijekom 4-tjednog strukturiranog sprinta kako bih naučio moderni Android stack. Koristi Jetpack Compose za korisničko sučelje, MVVM arhitekturu, Hilt za unos ovisnosti, Retrofit za dohvaćanje podataka iz Open Library API-ja i Room za offline predmemoriranje tako da aplikacija radi bez interneta.

Najveći izazov na koji sam naišao bila je pogreška u izradi Gradlea uzrokovana neusklađenošću verzija između Hilta i najnovijeg Android dodatka. Budući da su sukobi verzija vrlo česti u razvoju za Android, morao sam pregledati zapisnike izrade, pronaći nekompatibilnu ovisnost i popraviti je zaključavanjem dodatka na stabilnu verziju.

* **Gradle** je build alat koji spaja kod i biblioteke u konačnu aplikaciju.
* Gradle sve to skupa prevodi, spaja i spakira u jednu datoteku (`.apk`).
* Također, Gradle se brine o tome koje se verzije biblioteka koriste (`build.gradle.kts`).

---

## Odgovori na tehnička pitanja (Definicija + Projekt)

Ovo je nekolicina pitanja **(Stvarna definicija + Konkretan primjer iz koda)** podijeljenih po svim ključnim područjima:

### 1. Kotlin

**Null Safety u praksi:** Null-safety u Kotlinu sprječava `NullPointerException` na razini prevoditelja.
* *Konkretan primjer:* U API odgovoru `author_name` može biti `null` ako knjiga nema autora, pa sam koristio `authorName?.firstOrNull() ?: "Unknown author"` kako bi aplikacija ostala stabilna.

**Sealed Class vs Enum:** Enumi su primjereni za fiksne konstante s istim oblikom, dok `sealed class` dopušta da svako stanje nosi drugačije tipove podataka.
* *Konkretan primjer:* Koristio sam `sealed class UiState` jer `UiState.Success` sa sobom nosi `List<Book>`, `UiState.Error` nosi `String` poruku, a `UiState.Loading` ne nosi podatke.

### 2. Jetpack Compose

**Recomposition:** Recomposition je proces u kojem Compose ponovno izvršava samo one kompozabilne funkcije čiji su se ulazni podaci promijenili.
* *Konkretan primjer:* Kada Room baza ažurira podatke, `collectAsState()` u Compose-u to primijeti i automatski ponovno iscrta samo listu knjiga, bez ručnog pozivanja metoda za ažuriranje UI-ja.

**Remember vs ViewModel State:** State unutar `remember` preživljava rekompoziciju, ali se gubi kod promjene konfiguracije (npr. rotacija ekrana), dok ViewModel preživljava i rotaciju.
* *Konkretan primjer:* Unos u pretraživaču sam držao u ViewModelu kroz `StateFlow` kako korisnik ne bi izgubio utipkani tekst prilikom zakretanja mobitela.

### 3. Coroutines & Flow

**Launch vs Async:** `launch` se koristi za "fire-and-forget" zadatke koji ne vraćaju rezultat, dok `async` vraća `Deferred<T>` s rezultatom preko `.await()`.
* *Konkretan primjer:* U ViewModelu koristim `viewModelScope.launch` za pokretanje dohvata podataka iz baze, jer samo ažuriram `StateFlow` kao nuspojavu i ne trebam izravnu povratnu vrijednost.

**Zašto DAO vraća Flow:** `Flow` je asinhroni tok podataka koji emitira nove vrijednosti kad god se izvor podataka promijeni.
* *Konkretan primjer:* Moj `BookDao.getAllBooks()` vraća `Flow<List<BookEntity>>`. Čim pozadina (Retrofit) upiše nove podatke u Room, `Flow` automatski emitira novu listu prema ViewModelu i dalje prema UI-ju.

### 4. Hilt (Dependency Injection)

**@Provides vs @Inject constructor:** `@Inject constructor` se koristi kada imamo pristup klasi i njenom konstruktoru, dok `@Provides` koristimo u `@Module` klasama kada instanciramo vanjske biblioteke.
* *Konkretan primjer:* Moj `BookRepository` koristi `@Inject constructor`, ali za `Retrofit` i `RoomDatabase` morao sam napisati `@Module` s `@Provides` metodama jer ih gradim preko njihovih Builder obrazaca `Retrofit.Builder()...build()`.

### 5. Retrofit & Room (Networking & Data)

**Mapiranje JSON-a (@SerializedName):** Retrofit koristi konvertere (poput Gsona) za pretvaranje JSON-a u Kotlin objekte prema nazivu polja.
* *Konkretan primjer:* API vraća JSON u `snake_case` formatu (`cover_i`), pa sam koristio `@SerializedName("cover_i")` kako bih to u Kotlin DTO klasi mapirao na čistiji `camelCase` (`val coverId: Int?`).

**Single Source of Truth Pattern:** UI nikada ne prima podatke izravno s mreže, nego se mrežni podaci prvo spremaju u lokalnu bazu, a UI čita isključivo iz baze.
* *Konkretan primjer:* UI slika aplikacije čita podatke iz Room-a. Kada korisnik pokrene pretragu, Retrofit dohvati podatke s Open Library API-ja, zapiše ih u Room, a Room zatim automatski preko `Flow`-a osvježi UI. Ako nema interneta, prikazuju se zadnje spremljeni podaci.

### 6. Git Workflow

**Feature Branches & PR:** Rad na odvojenim granama omogućuje razvijanje novih funkcionalnosti bez narušavanja stabilnosti glavne (`main`) grane.
* *Konkretan primjer:* Za sortiranje knjiga preko naslova napravio sam granu `feature/sort-books`, odradio sve commitove, pushao na GitHub i kroz Pull Request spojio u `main`.

### 7. Architecture & Testing (Bonus)

**MVVM Arhitektura:** Odvaja poslovnu logiku i stanje (ViewModel) od samog prikaza (Compose UI) i pristupa podacima (Repository).
* *Konkretan primjer:* Compose ekran komunicira isključivo s `BookViewModel`-om preko `UiState`-a i ne zna ništa o tome dolaze li podaci iz baze ili s mreže.

**Zašto je ViewModel lako testirati:** Zato što ViewModel nema ovisnosti o Android kontekstu (`Context`) ili UI elementima. Pisanje testova za ViewModel je izuzetno brzo jer on ne ovisi o Android ekranu ni mobitelu.
* *Konkretan primjer:* `BookViewModel` ovisi samo o `BookRepository` sučelju, stoga sam pomoću biblioteke MockK napravio lažni Repository koji u sekundi vrati testne podatke. Tako sam na vlastitom računalu (JVM), bez pokretanja sporog emulatora, u sekundi provjerio radi li logika ViewModela ispravno.

**Preživljavanje rotacije (ViewModel Lifecycle):** Android uništava i ponovno stvara `Activity` prilikom rotacije ekrana, ali `ViewModel` ostaje u memoriji povezan s vlasnikom.
* *Konkretan primjer:* Testirao sam rotaciju uređaja tako što sam napravio dva brojača jedan pored drugog, jedan s remember state-om, jedan s ViewModelom, i rotirao ekran. `remember` se resetirao na 0, ViewModel je zadržao vrijednost, jer Android čuva istu ViewModel instancu kroz uništavanje/ponovno stvaranje Activityja koje rotacija uzrokuje.