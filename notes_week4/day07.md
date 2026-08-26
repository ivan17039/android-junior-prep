# Tjedan 4, Dan 7 – Detaljni plan: Generalna proba i sveobuhvatni pregled pitanja

## Sveobuhvatni pregled: sva pitanja iz checklista

### Kotlin

**Q: `val` vs `var`?**
`val` je referenca dodijeljena jednom (nepromjenjiva / immutable), dok se `var` može ponovno dodijeliti. *Reci na intervjuu:* "U mom projektu sva svojstva klase `Book` su `val`, jer se objekt knjige ne bi trebao mijenjati nakon što je stvoren. Ako trebam ažurirane podatke, radije stvorim novu instancu kroz funkcije za mapiranje u repozitoriju nego da mijenjam postojeću."

**Q: Kako radi null-safety (`?`, `!!`, `?:`, `?.let`)?**
`?` označava tip koji može biti null; `?.` sigurno poziva član samo ako primatelj nije null; `?:` (Elvis operator) daje zamjensku (fallback) vrijednost ako je lijeva strana null; `!!` prisilno otpakirava vrijednost i baca grešku ako je null. *Reci na intervjuu:* "Polje `author_name` u Open Library API-ju može biti null jer nema svaka knjiga navedenog autora. Zato koristim `authorName?.firstOrNull() ?: \"Nepoznati autor\"` — to je siguran poziv uz rezervnu opciju, pa nedostajući autor nikada ne sruši aplikaciju."

**Q: Što je `data class` i što automatski generira?**
Data class koristimo za podatke bez puno logike i s tom opcijom za razliku od normalnih vrijednosti ne vraća mjesto u memoriji gdje se nalazi određeni objekt već same vrijednosti tog objekta.
Generira `equals()` i `hashCode()` (usporedba po sadržaju/vrijednosti), `toString()`, `copy()`, te `componentN()` funkcije za rastavljanje (destrukturiranje) objekta. *Reci na intervjuu:* "Moje klase `Book`, `BookEntity` i varijante `UiState`-a su data klase. Upravo zato poziv `assertEquals(UiState.Success(books), viewModel.uiState.value)` radi izravno u mojim unit testovima, bez potrebe da ručno pišem metodu `equals()`."

**Q: Razlika između `==` i `===`?**
`==` poziva `equals()` (provjerava jesu li vrijednosti unutar objekata jednake), dok `===` provjerava referencijalnu jednakost (radi li se o doslovno istom objektu na istoj memorijskoj adresi). *Reci na intervjuu:* "Za moje data klase, `==` uspoređuje polje po polje. Dvijedijeljene instance klase `Book` sa svim istim vrijednostima bit će jednake preko `==`, ali neće biti jednake preko `===`."

**Q: Što su extension funkcije? Daj primjer.**
Dodaju novu funkcionalnost postojećem tipu podataka bez potrebe za izmjenom njegovog izvornog koda ili nasljeđivanjem. Pozivaju se istom sintaksom s točkom (`.`) kao i bilo koja druga metoda. *Reci na intervjuu:* Funkcije `OpenLibraryBookDto.toBook()` i `BookEntity.toBook()` u mom repozitoriju su proširive (extension) funkcije. One mi omogućuju da napišem `dto.toBook()` kao da je ta metoda oduvijek postojala na tom DTO objektu, iako ta klasa nije moja."

**Q: `sealed class` vs `enum class` — kad koristiš koje?**
`enum class` definira fiksni skup konstanti koje sve dijele identičan oblik i tip podataka. `sealed class` definira fiksni skup podtipova, ali svaki podtip može imati potpuno drugačija svojstva i podatke. *Reci na intervjuu:* "Koristio sam sealed klasu za `UiState` jer svakom stanju trebaju drugačiji podaci — `Success` nosi listu knjiga (`List<Book>`), `Error` nosi tekstualnu poruku o grešci, a `Loading` ne nosi ništa. Enum to ne bi mogao prikazati jer u njemu sve konstante moraju imati isti oblik."

**Q: Što je higher-order funkcija / lambda?**
Higher-order funkcija je funkcija koja prima drugu funkciju kao parametar ili je vraća kao rezultat. Lambda je anonimna funkcija koju možemo proslijediti kao vrijednost. *Reci na intervjuu:* "`viewModelScope.launch { }` prima lambdu kao zadnji argument — taj blok koda je tijelo korutine koje prosljeđujem. Slično tome, `books.map { it.toBook() }` prima lambdu koja opisuje kako transformirati svaki pojedini element."
`.map` je Higher-Order funkcija koja uzme listu i za svaki element izvrši uputu(lambdu).

**Q: Objasni `let` scope.*
**`let`** – izvrši blok samo ako vrijednost nije null (jamči da unutar bloka nema šanse da je vrijednost null) *Reci na intervjuu:* "Koristim `coverId?.let { \"[https://covers.openlibrary.org/b/id/$it-M.jpg](https://covers.openlibrary.org/b/id/$it-M.jpg)\" }` kod pretvaranja DTO-a u domenski model — `let` se izvršava i vraća URL samo ako `coverId` nije null."

---

### Osnove Androida (Android Fundamentals)

**Q: Objasni Activity lifecycle (onCreate → onDestroy).**
`onCreate` (jednokratno postavljanje) → `onStart` (postaje vidljivo na ekranu) → `onResume` (interaktivno, korisnik radi s njim) → `onPause` (djelomično zaklonjeno drugim elementom) → `onStop` (više nije vidljivo) → `onDestroy` (potpuno uklanjanje i čišćenje iz memorije). *Reci na intervjuu:* "Moja je aplikacija temeljena na jednoj Activity komponenti s Compose-om (single-Activity), pa se većina logike nalazi u Compose funkcijama i ViewModelu umjesto u samoj Activity. Ipak, oslanjam se na to da se `viewModelScope` automatski otkazuje kada se ViewModel čisti, što Android veže uz taj životni ciklus."

**Q: Objasni Fragment lifecycle — kako se razlikuje od Activity lifecyclea?**
Ima sličan slijed stanja, ali uz dodatne korake jer se korisničko sučelje (View) Fragmenta može uništiti i ponovno stvoriti neovisno o samoj instanci Fragmenta: npr. `onCreateView`/`onViewCreated` uz `onDestroyView` (UI nestaje, ali sam Fragment preživljava) prije završnog `onDestroy`. *Reci na intervjuu:* "Nisam koristio Fragmente u ovom projektu — sve je 100% Compose unutar jedne Activity. No znam da je razlika između `onDestroyView` i `onDestroy` ključna te da nepažnja s time često uzrokuje curenje memorije (memory leak)."

**Q: Activity vs Fragment — kad koristiti koje?**
Fragmenti su se povijesno koristili za višekratno iskoristive dijelove sučelja unutar jedne Activity (npr. prilagođeni prikazi za tablete) i za rad s Navigation Componentom. Danas u Compose aplikacijama Compose funkcije (Composables) preuzimaju tu ulogu, a arhitektura s jednom Activity komponentom postala je standard. *Reci na intervjuu:* "Moj projekt koristi jednu Activity i Compose ekrane između kojih se kreće pomoću Compose Navigation biblioteke — bez Fragmenta, što je moderna i preporučena praksa."

**Q: Što je `Context`? Application context vs Activity context?**
`Context` je pristupna točka prema resursima aplikacije i sustavskim uslugama. Application context živi koliko i sam proces cijele aplikacije, dok je Activity context vezan isključivo uz životni ciklus te jedne Activity. Pravilo: koristi Application context za sve što preživljava pojedinačni ekran (singletoni, baze), a Activity context za stvari vezane uz UI. Ako Activity context zadržiš duže nego što Activity živi, stvara se curenje memorije (memory leak). *Reci na intervjuu:* "Metoda `provideAppDatabase` u mom `DatabaseModule` modulu prima isključivo `@ApplicationContext`. Room baza je singleton koji nadživljava bilo koji pojedinačni ekran, pa joj treba Application context — u suprotnom bi došlo do curenja prve Activity koja je pokrenula stvaranje baze."

**Q: Što se dogodi kod rotacije ekrana i kako čuvaš stanje?**
Rotacija predstavlja promjenu konfiguracije uređaja; Android po zadanim postavkama potpuno uništi i ponovno stvori Activity. Obično stanje stvoreno s `remember` se gubi; stanje s `rememberSaveable` preživljava (sprema se u Bundle), dok ViewModel preživljava u potpunosti jer ostaje sačuvan u memoriji i ponovno se spaja na novu Activity. *Reci na intervjuu:* "To sam dokazao kroz praktični test — stavio sam dva brojača jedan pored drugog, jedan u `remember`, a drugi u ViewModelu preko `StateFlow`-a. Nakon rotacije uređaja, brojač iz `remember`-a se resetirao na 0, dok je brojač iz ViewModela zadržao svoju vrijednost jer Android čuva istu instancu ViewModela kroz ponovno stvaranje Activityja."

---

### Jetpack Compose

**Q: Što je composable funkcija?**
To je funkcija označena anotacijom `@Composable` koja deklarativno opisuje dio korisničkog sučelja. Umjesto ručnog crtanja, opisuje se *kako* UI treba izgledati za zadano stanje, a Compose sam brine o učinkovitom ažuriranju prikaza kada se podaci promjene. *Reci na intervjuu:* "`BookCard` i `BookListScreen` su composable funkcije — `BookCard` samo prima objekt `Book` i opisuje njegov izgled; nikada ne poziva ručne metode za izmjenu prikaza."

**Q: Što je recomposition i što je pokreće?**
To je proces u kojem Compose ponovno izvršava samo one composable funkcije čiji su se ulazni podaci (stanje) promijenili — bez osvježavanja cijelog stabla sučelja. *Reci na intervjuu:* "Kada se moja Room baza ažurira nakon poziva `refreshBooks()`, `collectAsState()` u `BookListScreen`-u to prepozna, i Compose automatski ponovno iscrta samo one dijelove ekrana koji prikazuju te podatke — bez imalo ručnog koda za osvježavanje."

**Q: `remember` vs `rememberSaveable` — razlika?**
Obje funkcije pamte vrijednost kroz rekompozicije. Međutim, `remember` gubi podatke kada se Activity uništi i ponovno stvori (npr. kod rotacije ekrana) ili kod gašenja procesa od strane sustava. `rememberSaveable` dodatno pohranjuje vrijednost u Bundle, pa podaci preživljavaju i te promjene. *Reci na intervjuu:* "To je točno ona razlika koju sam prikazao u primjeru s dva brojača — brojač s `remember` funkcijom izgubio je vrijednost rotacijom, dok bi s `rememberSaveable` ili u ViewModelu vrijednost ostala sačuvana."

**Q: Što je state hoisting i zašto je bitan?**
State hoisting je uzdizanje stanja izvan composable funkcije kako bi ona postala "stateless" (bez vlastitog stanja). Funkcija tada samo prima trenutne podatke i callback događaje, što je čini lakšom za ponovno korištenje, testiranje i prikaz u Compose Previewu. *Reci na intervjuu:* "`BookCard` ne čuva nikakvo stanje u sebi — prima `Book` i `onClick` lambdu. Stvarno stanje nalazi se više u `BookListViewModel`-u. Upravo zato `BookCard` mogu lako testirati i prikazati u radnom okruženju s bilo kojim lažnim podacima, potpuno neovisno o mreži ili bazi."

**Q: Kako se Compose temeljno razlikuje od starog XML/View sustava?**
Stari View sustav je imperativan — ručno tražiš view element po ID-u i pozivaš metode za njegovu izmjenu svaki put kad se podaci promijene. Compose je deklarativan — opisuješ sučelje kao funkciju trenutnog stanja, a sam framework odrađuje usporedbu i ponovno crtanje. *Reci na intervjuu:* "Ažuriranje liste knjiga na ekranu svodi se na to da ViewModel ažurira `_uiState`, a Compose na to automatski reagira preko `collectAsState()`. U cijelom projektu nemam nijedan `findViewById` niti ručno mijenjanje izgleda."

---

### Coroutines & Flow

**Q: Coroutine vs thread?**
Korutina je lagana jedinica asinhronog rada s kooperativnim raspoređivanjem koja se može privremeno zaustaviti (suspendirati) i nastaviti bez blokiranja same niti (threada). Velik broj korutina može raditi na samo nekoliko stvarnih niti. Nit (thread) je znatno teži resurs samog operacijskog sustava. *Reci na intervjuu:* "Svaki poziv `viewModelScope.launch { }` u mom ViewModelu pokreće korutinu, a ne novu dretvu. Rad s mrežom i bazom u pozadini i dalje se odvija na pozadinskim niti preko `Dispatchers.IO` unutar Retrofita i Room-a, ali ja ne moram ručno upravljati niti jednim threadom."

**Q: Što znači `suspend`?**
Označava funkciju koja se može pauzirati u određenom trenutku (bez blokiranja dretve) i kasnije nastaviti s radom. Može se pozvati samo iz druge suspend funkcije ili unutar pokrenute korutine. *Reci na intervjuu:* "Funkcije `ApiService.searchBooks()` i `BookRepository.refreshBooks()` su obje definirane kao `suspend`. Ta se suspendabilnost mora prenositi kroz sve funkcije u lancu poziva, sve do mjesta gdje se korutina konačno pokrene unutar `viewModelScope.launch`."

**Q: Čemu služe `Dispatchers.Main` / `IO` / `Default`?**
`Main` je namijenjen radu na glavnoj dretvi korisničkog sučelja (UI); `IO` je optimiziran za ulazno-izlazne radnje poput mrežnih zahtjeva ili pristupa bazi i disku; `Default` je namijenjen za računski zahtjevnije zadatke (obrada podataka, sortiranje, parsiranje). *Reci na intervjuu:* "Nisam morao ručno prebacivati dispečere — Retrofit i Room sami preusmjeravaju svoj rad na `Dispatchers.IO`, zbog čega mogu pozvati `repository.refreshBooks()` izravno unutar `viewModelScope.launch` bez ikakvog zamrzavanja sučelja."

**Q: Što je `viewModelScope`?**
To je `CoroutineScope` izravno vezan uz životni ciklus ViewModela. Sve korutine pokrenute unutar njega automatski se otkazuju i čiste čim se ViewModel ukloni iz memorije. *Reci na intervjuu:* "I promatranje podataka i osvježavanje u `BookListViewModel`-u pokreću se unutar `viewModelScope`-a. Ako korisnik zatvori ekran i ViewModel se očisti, sve korutine se automatski prekidaju bez opasnosti od curenja resursa."

**Q: `launch` vs `async` — kad koristiš koje?**
`launch` pokreće korutinu i vraća `Job` objekt; koristi se kada nam ne treba izravan povratni rezultat ("opali i zaboravi"). `async` vraća `Deferred<T>` i koristi se kada nam TREBA povratna vrijednost koju dohvaćamo pozivom `.await()`. *Reci na intervjuu:* "U ViewModelu sam koristio `launch` jer metoda `refreshBooks()` ne mora vratiti vrijednost na mjestu poziva, već samo ažurira `_uiState`. Poziv `async` iskoristio bih da moram paralelno povući podatke s dva različita API-ja i spojiti njihove rezultate."

**Q: Što je structured concurrency (strukturirano paralelno izvršavanje)?**
To je princip u kojem se sve korutine pokreću unutar točno definiranog opsega (scopea), pri čemu je taj opseg odgovoran za sve svoje podređene korutine. Otkazivanje glavnog opsega automatski otkazuje sve što je unutar njega pokrenuto. *Reci na intervjuu:* "`viewModelScope` je pravi primjer strukturiranog paralelnog izvršavanja — ne moram ručno pratiti i zaustavljati pokrenute zadatke pri zatvaranju ekrana, jer otkazivanje opsega automatski prekida sve podređene korutine."

**Q: `Flow` vs `StateFlow` vs `SharedFlow` — razlike i kad koristiti?**
`Flow` je "hladni" asinhroni tok podataka koji počinje raditi i emitirati tek kada ga netko počne sakupljati (collect). `StateFlow` je "vrući" tok koji uvijek čuva i nudi trenutnu vrijednost te je odmah proslijeđuje novom sakupljaču — idealno za prikaz UI stanja. `SharedFlow` je vrući tok namijenjen za emitiranje jednokratnih događaja (npr. poruke ili obavijesti) prema više promatrača. *Reci na intervjuu:* "`BookDao.getAll()` vraća hladni `Flow` — upit nad bazom izvršava se tek kad ga počnemo pratiti. ViewModel izlaže `uiState` kao `StateFlow` jer sučelju uvijek treba trenutno stanje za trenutačni prikaz, a novi sakupljač nakon rotacije ekrana odmah dobiva zadnju dostupnu vrijednost."

**Q: Kako se hvataju iznimke unutar korutine?**
Standardni `try/catch` blok unutar tijela korutine radi normalno za očekivane greške. Neuhvaćene iznimke šire se prema gore i mogu otkazati cijeli obuhvaćajući opseg. `CoroutineExceptionHandler` se može postaviti na razini opsega za hvatanje nepredviđenih grešaka. *Reci na intervjuu:* "Moja metoda `refreshBooks()` omotava mrežni poziv u običan `try/catch` blok jer želim lokalno presresti grešku i pretvoriti je u `UiState.Error` s odgovarajućom porukom, umjesto da pustim da iznimka prekine ostale zadatke."

---

### Dependency Injection (Hilt / Koin)

**Q: Što je dependency injection (ubrizgavanje ovisnosti) i zašto se time baviti?**
To je uzorak u kojem klasa dobiva objekte o kojima ovisi izvana, umjesto da ih sama stvara unutar svog koda. Prednosti su znatno lakše testiranje (prave objekte možemo zamijeniti lažnima) i odvajanje klase od znanja o tome *kako* se njezine ovisnosti uopće konstruiraju. *Reci na intervjuu:* "Moj `BookListViewModel` nikada sam ne stvara `BookRepository` — samo deklarira da mu je potreban, a Hilt mu ga isporuči. To mi je omogućilo da u unit testovima bez problema podmetnem lažni repozitorij (`mockk<BookRepository>()`) bez mijenjanja ijedne linije koda u ViewModelu."

**Q: Kako Hilt radi na visokoj razini (`@HiltAndroidApp`, `@Inject`, `@Module`, `@Provides`)?**
Anotacija `@HiltAndroidApp` na Application klasi inicijalizira cijeli graf ovisnosti. Anotacija `@Inject` na konstruktoru govori Hiltu kako stvoriti tu klasu. `@Module` i `@Provides` koriste se za objekte koje Hilt ne može samostalno izraditi putem konstruktora (sučelja, vanjske biblioteke). *Reci na intervjuu:* "U mom projektu `BookRepository` i `BookListViewModel` koriste `@Inject constructor`. S druge strane, `ApiService`, `Retrofit`, `BookDao` i `AppDatabase` zahtijevaju `@Module` i `@Provides` metode jer se radi o sučeljima ili objektima koji se grade preko Builder obrazaca."

**Q: Što znači scoping (opseg trajanja) ovisnosti (singleton vs po ekranu)?**
Scoping određuje koliko dugo pojedina instanca objekta živi u memoriji i dijeli li se s drugima. `@Singleton` znači da postoji samo jedna instanca za cijelo vrijeme rada aplikacije. `@HiltViewModel` stvara instancu koja je vezana uz životni ciklus ekrana koji je zatraži. *Reci na intervjuu:* "Moji `Retrofit`, `AppDatabase`, `ApiService` i `BookDao` su svi `@Singleton` — želim točno jednu vezu prema bazi i jedan mrežni klijent za cijelu aplikaciju. Sam `BookListViewModel` ima opseg vezan uz ekran koji ga poziva preko `hiltViewModel()`."

---

### Arhitektura i čisti kod (Architecture & Clean Code)

**Q: Objasni MVVM — što radi svaki sloj?**
UI sloj (Compose) prikazuje trenutno stanje i prosljeđuje korisničke akcije. ViewModel drži stanja sučelja i poslovnu logiku, preživljava rotaciju ekrana i nema nikakvu referencu na UI elemente. Model sloj (Repository i izvori podataka) odgovoran je za dobivanje i upravljanje samim podacima. *Reci na intervjuu:* "Moj Compose ekran samo čita `UiState` iz ViewModela i šalje klikove. ViewModel uopće ne zna da Compose postoji, a sloj podataka ne zna da ViewModel postoji. Svaki sloj komunicira isključivo sa slojem izravno ispod sebe."

**Q: Zašto ViewModel ne bi trebao držati referencu na View/Activity/Context?**
ViewModel je dizajniran da nadživi pojedinačnu Activity instancu (npr. kod rotacije). Ako bi držao referencu na Activity ili View, ta bi referenca spriječila čišćenje stare Activity iz memorije, što uzrokuje curenje memorije (memory leak). *Reci na intervjuu:* "Moj ViewModel komunicira samo s repozitorijem — nikada ne prima `Context`, `Activity` niti bilo koji Compose objekt. On samo izlaže podatke putem `StateFlow`-a, a ekran to promatra. Ovisnost ide samo u jednom smjeru."

**Q: Što je "separation of concerns" (odvajanje odgovornosti) i zašto je bitan u praksi?**
To je princip gdje svaki dio koda ima jednu točno definiranu odgovornost. Kod je lakši za čitanje, održavanje i testiranje jer izmjena u jednom sloju ne zahtijeva zahvate u drugima. *Reci na intervjuu:* "To je razlog zašto sam uveo sloj Repozitorija umjesto da izravno pozivam mrežne servise iz ViewModela. Miješanje UI logike i mrežnih poziva otežava testiranje, a uvođenje lokalnog predmemoriranja (Room) kasnije je prošlo bez ikakvih izmjena u samom ViewModelu."

**Q: Što je Repository pattern?**
To je sloj koji skriva stvarni izvor podataka (mreža, lokalna baza, memorija) iza čistog sučelja. Potrošači podataka (poput ViewModela) ne moraju znati odakle podaci stvarno dolaze. *Reci na intervjuu:* "`BookRepository` izlaže samo `books: Flow<List<Book>>` i metodu `refreshBooks(query)`. ViewModel uopće ne mora znati da se iza toga nalaze Retrofit i Room ili kako su oni međusobno usklađeni."

**Q: Kako bi strukturirao malu aplikaciju po slojevima (UI/domain/data)?**
UI sloj prikazuje sučelje i reagira na dodire. Data sloj zna *kako* i *odakle* dohvatiti podatke (baza, API). Domain sloj sadrži čiste poslovne modele i logiku neovisnu o izvorima podataka. *Reci na intervjuu:* "UI sloj su Compose funkcije. Data sloj čine `ApiService`, `BookDao`, baza i sirovi DTO/Entity objekti. Moja `Book` data klasa predstavlja čisti domenski model u koji se svi podaci pretvaraju, neovisno dolaze li s mreže ili iz lokalne baze."

**Q: Što čini kod "testabilnim" — koje odluke pomažu ili odmažu?**
Pomažu: ubrizgavanje ovisnosti (DI), male klase s jednom odgovornošću i odvajanje logike od Android okvira. Odmažu: klase koje same stvaraju svoje ovisnosti, izravna vezanost uz `Context` ili `View`, te velike klase koje rade previše stvari odjednom. *Reci na intervjuu:* "To sam uvidio kada sam pisao unit testove — obrada prazne liste u ViewModelu imala je rubni slučaj koji je unit test odmah otkrio. To je bilo moguće jer su sve ovisnosti ubrizgane i mogu se lako simulirati u testu za nekoliko milisekundi."

---

### Mreža i API (Networking / Retrofit)

**Q: Kako Retrofit radi na visokoj razini?**
Definira se sučelje s anotacijama (`@GET`, `@POST` itd.) koje opisuju mrežne zahteve. Retrofit automatski generira implementaciju koja pozive pretvara u prave HTTP zahtjeve, dok pretvarač (npr. Gson) pretvara dobiveni JSON izravno u Kotlin objekte. *Reci na intervjuu:* "Moje `ApiService` sučelje ima `@GET` funkciju. Retrofit pretvara poziv `searchBooks(query)` u stvarni mrežni zahtjev, a Gson pretvara JSON odgovor direktno u moje `SearchResponse` i `OpenLibraryBookDto` objekte."

**Q: Što je REST, ukratko?**
Arhitektonski stil za izradu web API-ja gdje se s resursima (identificiranim putem URL-ova) komunicira pomoću standardnih HTTP metoda (GET, POST, PUT, DELETE), najčešće razmjenjujući podatke u JSON formatu. *Reci na intervjuu:* "Moja aplikacija koristi GET zahtjeve jer samo čita javne podatke o knjigama. Da sam razvijao vlastiti sustav s korisničkim računima i spremnima, dodao bih POST, PUT i DELETE metode."

**Q: Kako rukuješ mrežnim greškama, prekidima veze i timeoutima?**
Mrežni poziv se omotava u `try/catch` blok gdje se prepoznaju specifične iznimke (npr. `UnknownHostException` za nedostatak interneta, `SocketTimeoutException` za predugo čekanje) kako bi se korisniku prikazala jasna poruka. Idealno je primijeniti lokalno predmemoriranje kako aplikacija i u slučaju greške prikazuje zadnje spremljene podatke. *Reci na intervjuu:* "Moja funkcija `mapErrorMessage()` pretvara tip iznimke u razumljivu poruku na hrvatskom. ViewModel prikazuje grešku samo ako u bazi već nema spremljenih podataka — tako privremeni gubitak mreže ne briše već učitanu listu."

**Q: Koje biblioteke za JSON parsiranje poznaješ (Gson, Moshi, kotlinx.serialization)?**
Gson radi pomoću refleksije, jako je raširen i jednostavan za početak. Moshi je sličan, razvijen od tima iza Retrofita, s boljom podrškom za Kotlin null-safety. `kotlinx.serialization` je službeno JetBrains rješenje koje radi u fazi prevođenja (bez refleksije), što ga čini najbržim i najsigurnijim rješenjem. *Reci na intervjuu:* "Koristio sam Gson jer je standardan i brz za postavljanje. Za novi produkcijski projekt danas bih odabrao `kotlinx.serialization` zbog sigurnosti pri kompajliranju."

---

### Testiranje i Git (Testing & Git)

**Q: Unit testovi vs UI/Instrumentation testovi — razlika?**
Unit testovi se izvode na lokalnom računalu (JVM), ne trebaju Android uređaj i iznimno su brzi — odlični su za provjeru logike u izolaciji. Instrumentation / UI testovi zahtijevaju pokrenut emulator ili stvarni mobitel, sporiji su, ali testiraju stvarno ponašanje ekrana i sučelja. *Reci na intervjuu:* "Svi moji testovi su unit testovi locirani u `app/src/test` direktoriju. Zbog dobre arhitekture ni ViewModel ni Repozitorij ne ovise izravno o Android okviru, pa se svi testovi izvršavaju u nekoliko sekundi na računalu."

**Q: Kako testirati ViewModel koji koristi korutine?**
Potrebno je zamijeniti glavnog dispečera (`Dispatchers.Main`) testnim dispečerom (`Dispatchers.setMain(...)`), pokrenuti test unutar `runTest { }` bloka te upotrijebiti `advanceUntilIdle()` kako bi sve pokrenute korutine završile prije nego što provjerimo rezultate. *Reci na intervjuu:* "To je standardna priprema u mom `BookListViewModelTest`-u — prije svakog testa postavljam testni dispečer preko `Dispatchers.setMain`, a nakon testa ga poništavam s `resetMain`. Tako `viewModelScope.launch` stabilno radi u testnom okruženju."

**Q: Što je mockanje (simulacija) i koje biblioteke koristiš?**
To je stvaranje lažnog objekta koji zamjenjuje pravu ovisnost, s punom kontrolom nad time što će taj objekt vratiti ili koju će grešku baciti. To omogućuje izolirano testiranje jedne klase bez stvarne mreže ili baze. *Reci na intervjuu:* "Koristio sam MockK biblioteku — s `mockk<BookRepository>()` i naredbama `coEvery` definirao sam što lažni repozitorij vraća, dok sam s `coVerify` provjeravao je li se određena metoda stvarno pozvala s točnim parametrima."

**Q: `git merge` vs `git rebase`?**
`merge` spaja dvije grane stvaranjem novog spojenog (merge) commita i u potpunosti čuva izvorni povijesni slijed. `rebase` prebacuje tvoje commitove na sam vrh ciljne grane, dajući čišću i linearnu povijest, ali mijenja postojeću povijest commitova — što može biti rizično na zajedničkim granama. *Reci na intervjuu:* "Koristio sam klasični `merge` kada sam dodavao funkcionalnost sortiranja knjiga. To je siguran pristup, a razumijem i `rebase` te razlike među njima u timskom radu."

**Q: Što je merge conflict (sukob pri spajanju) i kako ga rješavaš?**
Nastaje kada Git ne može automatski spojiti izmjene jer su iste linije koda mijenjane na različite načine u obje grane. Problem se rješava ručnim otvaranjem datoteke, odabirom ispravnog koda, uklanjanjem Git oznaka sukoba i novim commitom. *Reci na intervjuu:* "Namjerno sam vježbao taj scenarij — izazvao sam sukob izmjenom iste linije u `main` grani i u pomoćnoj grani, a zatim ručno razriješio sukob kako bih bio spreman za stvarni rad u timu."

**Q: `git pull` vs `git fetch`?**
`git fetch` samo preuzima nove podatke i grane s udaljenog poslužitelja (GitHub), ali ne dira tvoj lokalni kod niti išta spaja. `git pull` je zapravo `fetch` nakon kojeg se odmah izvršava `merge` u tvoju trenutnu granu. *Reci na intervjuu:* "U mom svakodnevnom radu poziv `git pull` na `main` grani brzo i jednostavno dohvaća i spaja sve zadnje izmjene s poslužitelja."

---

### Vjerojatni praktični zadaci i rad uživo

Ove stavke predstavljaju zadatke za provjeru znanja uživo:

* **Objašnjenje arhitekture projekta** — Jasno izlaganje strukture aplikacije (MVVM, Hilt, Retrofit, Room, Compose) uz navođenje primjera iz vlastitog koda.
* **Mali algoritamski zadatak** — Osnovni zadaci obrade podataka (npr. okretanje tekstualnog niza, pronalaženje duplikata u listi ili filtriranje).
* **Pregled ponuđenog koda ("Pronađi grešku")** — Najčešće se radi o curenju memorije zbog krivog konteksta ili nedostajućoj null-provjeri. Važno je razmišljati naglas i objašnjavati korake pri pronalasku problema.
* **Izrada jednostavnog Compose ekrana uživo** — Pisanje jednostavne `LazyColumn` liste s prikazom elemenata i osnovnim kretanjem između ekrana.
* **Objašnjenje stvarnog problema/buga** — Pripremljena **dva stvarna primjera iz projekta**:
1. *Problem s rušenjem baze pri migraciji (Room migration crash)* — Stvarni problem s baze koji je identificiran i riješen analizom zapisnika (Logcat).
2. *Rubni slučaj s praznom listom (Empty list edge-case)* — Logički problem koji je uočen i ispravljen pisanjem unit testova prije nego što je dospio u produkciju.