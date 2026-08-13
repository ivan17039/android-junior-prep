# Tjedan 3 – Finalni pregled: sva pitanja i cijeli data flow

## Dan 1 – Retrofit setup i definicija API-ja

**1. Što znače GET, POST, PUT, DELETE u kontekstu REST API-ja?**
To su metode odnosno zahtjevi kojima upravljamo podacima na serveru.

* **GET:** Dohvaćanje podataka (Read).
* **POST:** Stvaranje novih podataka (Create).
* **PUT:** Ažuriranje/zamjena postojećih podataka (Update).
* **DELETE:** Brisanje podataka (Delete).

**2. Kako Retrofit zna kako mapirati JSON polja u polja Kotlin data classe?**
Retrofit odnosno alat (GSON) automatski mapira polja po istom imenu.
Tako što se usporede imena JSON polja s imenima svojstava data class-e.
Ako se imena ne poklapaju potrebna je dodatna anotacija.

---

## Dan 2 – Repository sloj i spajanje na Hilt

**1. Što bi se zakompliciralo da ViewModel direktno poziva Retrofit servis, bez repositorija?**
Bez repositorija ne bi se znali izvori podataka kao i logika dohvaćanja podataka.
Tako npr. ViewModel ne bi znao odakle uzeti podatke — s mreže ili lokalne Room baze.
Ako više ViewModela treba iste podatke potrebno je ponavljati mrežne pozive što dodatno usporava rad aplikacije.
Bez pravog pozivanja API-ja preko repositorija ne bi se znalo napisati Unit test.

**2. Koja je razlika između @Provides i @Inject constructor u Hiltu (barem osnovna ideja)?**
`@Provides` se koristi kod Hilt modula kada ne posjedujemo klasu ili za sučelja, vanjske biblioteke odnosno kad Hilt ne može jednostavno pozvati konstruktor.
`@Inject constructor` koristimo kada obrađujemo vlastitu klasu s normalnim konstruktorom i imamo pristup njezinom konstruktoru.

---

## Dan 3 – Zamjena hardkodiranih podataka pravim API pozivom

**1. Kroz koje sve slojeve prolazi jedan poziv, od klika korisnika do prikaza podatka na ekranu?**
Imamo suspend lanac: funkcija na dnu lanca poziva mrežni poziv stoga se suspend mora prenijeti sve do ViewModela.
Sloj započinje ApiService-om koja je suspend (Retrofit mrežni poziv), nakon toga ide Repository koji je isto suspend (zbog poziva apiService-a) i za kraj imamo ViewModel koji jedini nije suspend ali pokreće korutinu preko `viewModelScope.launch{}` za poziv suspend funkcije Repository-a.

---

## Dan 4 – Učitavanje slika (Coil)

**1. Zašto se za učitavanje slika s interneta koristi biblioteka poput Coila umjesto ručnog downloada i dekodiranja?**
Zbog toga što se za ručni pristup zahtjeva niz operacija: otvaranje mrežne veze, preuzimanje sirovih bajtova i dekodiranje istih preko Bitmap-a pa onda prikaz i bitno se pobrinuti za memoriju/cache (spremanje slike kad se skrola natrag), otkazivanje i dodatne greške.

**2. Čemu služi placeholder, a čemu error slika?**
Placeholder služi za prikaz dok se slika još učitava, a error ako učitavanje ne uspije zbog lošeg URL-a ili veze interneta.

---

## Dan 5 – Lokalna baza podataka (Room)

**1. Što znači "single source of truth" pattern u kontekstu mreže i lokalne baze?**
SSOT znači da lokalna baza podataka služi kao jedini izvor istine za UI. UI nikad ne čita podatke izravno sa mreže nego kad stignu novi podaci sa API-ja oni se prvo spremaju u lokalnu bazu, a UI automatski reagira i prikazuje te podatke iz baze.

**2. Zašto je korisno da DAO vraća Flow, a ne običnu listu?**
Zato što Flow automatski emitira novu listu i UI se osvježi nakon što se tablica promijeni. Obična lista zahtijevala bi niz poziva zbog toga što je suspend i vraća listu samo jednom.

**3. Što bi se dogodilo s korisničkim iskustvom da app uopće nema lokalni cache?**
Korisničko iskustvo prikazalo bi grešku s kojom ne može dohvatiti podatke sa servera, odnosno da ne postoji adresa povezana s hostname-om.

---

## Dan 6 – Error handling

**1. Koje sve stvari mogu poći po zlu kod mrežnog poziva, i je li ih realno sve pojedinačno hvatati?**
Može doći do toga da uređaj nema mrežnu vezu, odnosno da se ne može dohvatiti host.
Postoji i situacija u kojoj veza uopće ne postoji ili da mreža presporo odgovara.
Također server može odgovoriti sa greškom kao što je 404 da ne postoji ili 500 server pokvaren.
Ima još deseci rubnih slučajeva, ali ovo su najčešći.

**2. Zašto je bolje korisniku prikazati razumljivu poruku greške nego da app jednostavno "puca" (crash)?**
To bi se smatralo lošom aplikacijom, rušenje aplikacije uništi povjerenje korisnika, dok jasna poruka (npr. "Nema internetske veze. Pokušajte ponovno.") daje korisniku do znanja što se događa i kako to riješiti.

---

## Dan 7/8 – Testiranje i Git workflow

**1. Zašto je lakše testirati ViewModel nego Composable UI direktno?**
Zbog toga što ViewModel možemo testirati bez telefona, ekrana ili klikanja.

**2. Što bi se dogodilo da ViewModel direktno instancira Repository (Repository()) umjesto da ga dobiva kroz DI — kako bi to otežalo testiranje?**
To je zahtjevno i sporo, zato uvodimo lažni mock Repository kojem se ručno govori što treba vratiti.

**3. Zašto timovi rade preko feature grana i pull requestova umjesto direktno na main grani?**
Zato jer main grana treba biti u svakom trenutku ispravna i u radnom stanju. Nakon što se provjeri da feature grana radi, preko git merge se poveže s glavnom.

**4. Što je code review i zašto je koristan i za autora koda, ne samo za tim?**
Code review je postupak kojim netko provjerava promjene prije spajanja u main, tražeći greške, nejasnoće ili bolji pristup.
Korisno je za pronalazak grešaka prije nego dođu do korisnika, s time se uključuje dodatno opisivanje pull requesta.

# Cijeli data flow

## Glavno

UI (ekran) čita podatke isključivo iz lokalne baze (Room). Nikada ne gleda na internet.  
Internet (API) služi isključivo tome da napuni lokalnu bazu novim podacima.  
NEMA direktne komunikacije između ekrana i interneta.

## Kratko sažeti kako sad izgleda cijeli data flow: UI → ViewModel → Repository → Retrofit/Room → API/lokalna baza

### (A) korisnik otvori app s internetom

#### 1. Pokretanje

Korisnik otvara aplikaciju.  
Ekran treba podatke stoga se stvara ViewModel.  
Compose ekran (UI) poziva funkciju za stvaranje ViewModela (npr. `hiltViewModel()`).

Alat Hilt mu odmah u pozadini da pristup lokalnoj bazi (Room) i mreži (Retrofit).  
"davanje u pozadini" zovemo Dependency Injection (DI) => klasa zatraži što joj treba u parametru konstruktora, a netko drugi (Hilt) pribavi podatke.

U kodu to vidimo tako što iznad ViewModela stoji `@HiltViewModel`,  
a u njegov konstruktor (kroz `@Inject`) Hilt sam ubaci gotov Repository.

#### 2. Spajanje na bazu

ViewModel se odmah priključi na bazu i sluša što ima u njoj.  
Priključi znači da se taj kod nalazi u `init {}` bloku unutar ViewModela.  
Slušanje baze znači da poziva funkciju sa Repository-jem koji vraća `Flow` na koji se dodaje `.collect {}`.

#### 3. Prvi prikaz na ekranu

Ekran prikazuje što je ViewModel našao u bazi.  
Ako još nema ništa vrti se loading screen.  
Ovo je State Management.  
ViewModel spremi rezultat baze u svoj `UiState`, a ekran uz pomoć `collectAsState()` provjerava `UiState` (`Loading`, `Success`, `Error`).

#### 4. Odlazak na internet

U isto vrijeme dok sluša bazu ViewModel kaže Repository-u da dohvati nove podatke s interneta.  
Isto vrijeme znači Corutine (asinkrono).  
ViewModel u kodu pokreće `viewModelScope.launch {}` za obradu mrežnog poziva u pozadini bez da ekran smrzne.

#### 5. Skidanje i spremanje

Server vraća nove podatke koje se prvo zapišu u bazu (Room) prije prikazivanja na ekranu.  
(SSOT)  
Podaci s weba (JSON/DTO) stižu u Repository koji ih pretvara u baznične oblike (Entity) i poziva Room DAO funkciju da ih spremi u lokalnu tablicu.

#### 6. Obavještenje iz baze

Budući da ViewModel sluša (korak 2), čim se novi podaci spreme baza automatski javi da ima novosti i pošalje mu.  
Kad god se podaci preko `@Insert` ubace u tablicu kod u Room-u automatski emitira novu listu kroz `Flow` kanal koji ViewModel osluškuje od 2 koraka.

#### 7. Osvježavanje ekrana

ViewModel proslijedi nove podatke na ekran koji se osvježi i korisnik vidi najnovije podatke s interneta.  
ViewModel ažurira `StateFlow` novom listom koju je dobio.  
Compose ekran to primjeti i automatski okine Rekompoziciju - ponovno iscrtavanja ekrana.

---

### (B) korisnik otvori app bez interneta ali s postojećim cache-om

#### Pokretanje i spajanje na bazu

Korisnik otvori aplikaciju.  
ViewModel se kreira i odmah počinje slušati bazu (Room).

#### Brzi prikaz starih podataka

Budući da baza ima spremljene podatke od zadnjeg puta ona ih iste sekunde pošalje.  
Ekran ih prikaže stoga korisnik ne mora čekati.

#### Pokušaj spajanja na mrežu

ViewModel pokuša rutinski otići na Internet po nove podatke, ali nema interneta pa pokušaj propadne.

#### Baza je sigurna

S obzirom da se ništa nije uspjelo skinuti aplikacija uhvati grešku i ništa se ponovno ne zapisuje u bazu.  
Baza je netaknuta i ekran idalje normalno prikazuje stare podatke.

#### Poruka korisniku

Zbog greške u mreži ViewModel samo proslijedi signal ekranu da na dnu iskoči mala poruka "Nema interneta, prikazuju se stari podaci".