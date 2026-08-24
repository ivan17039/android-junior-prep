# Tjedan 4, Dan 4 – README i prezentacija projekta

---

## Zašto je dobar README presudan?

1. **Prvi dojam (30 sekundi):** Većina ljudi neće klonirati i pokrenuti kod. README sa slikama dokazuje da aplikacija stvarno radi.
2. **Kratki pregled tehnologija:** Omogućuje brzo spajanje (*pattern-matching*) s onim što se traži u oglasu za posao (Kotlin, Compose, Hilt, Room).
3. **Prikaz zrelosti ("Što bih sljedeće dodao"):** Priznavanje ograničenja (npr. fiksirani upit pretraživanja) pokazuje razumijevanje prioriteta i zrelost, a ne nedostatak znanja.

---

## Ključni dijelovi README datoteke

* **Visual Badges:** Kratki grafički pečati na vrhu (`shields.io`) za vizualno isticanje staka (`Kotlin`, `Android`, `Compose`, `Room`).
* **HTML Tablica za slike:** Korištenje `<img src="..." width="180px" />` unutar tablice umjesto standardnog Markdowna radi kontrole veličine screenshotova.
* **Arhitektura:** Jasno objašnjen *Single Source of Truth* princip (UI čita iz Rooma, mreža samo ažurira Room).
* **Iskreni "Next Steps":** Popis nadogradnji (stvarno polje za pretraživanje, učitavanje novih knjiga tijekom skrolanja, tamna tema).

---

## Zadatak

**Pitanje: Zašto je dobro u README dodati sekciju "Što bih sljedeće dodao" umjesto da glumiš da je projekt savršen?**

**Odgovor:** Pokazuje da sam svjestan ograničenja u razvoju softvera. Puno je bolje prvi istaknuti što fali i ponuditi plan nadogradnje nego ostaviti utisak da nisam primijetio nedostatke ili da ih pokušavam sakriti.