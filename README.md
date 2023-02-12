# Blackjack mit Server & Client Struktur
## Softwareprojekt 2022-2023 HTL-Traun

## Screenshot:
![App GUI](https://raw.githubusercontent.com/Rudi-Wagner/Blackjack/main/Java_Blackjack/Docs/App_GUI.JPG)
 
## How to Install
 1. Das neueste Release-Paket herunterladen und entpacken.
 2. Darin enthalten sind zwei ausführbare Jar Dateien, der Client und der Server.
 
## How to Use
#### Server
- Der Server wird über die zugehörige Batch-Datei gestartet, damit die Serverlogs angezeigt werden.
- Alternativ kann auch direkt die Jar-Datei gestartet werden, dort gibt es aber keine Oberfläche und keine Konsole.
- Die Jar-Datei kann auch mit einem Übergabeparameter, der den Port bestimmt, gestartet werden.
- Der Server erstellt den Socket standardmäßig am localhost auf Port 6868.
#### Client
- Der Client kann direkt über die Jar-Datei gestartet werden, da dieser eine GUI besitzt.
- Oder wieder über die zugehörige Batch-Datei für die Clientlogs.
- Der Spieler muss, um sich zu einem Server zu verbinden, den Hostname und den Port über das Startfenster angeben.

## Beschreibung
Ein einfaches Blackjack-Kartenspiel mit einer Server-Client Struktur.
 - Der Server Hostet das Spiel und fungiert als Dealer, der jedem Spieler die Karten gibt. 
 - Der Client, also ein Spieler, verbindet sich zum Server und fordert die Karten an.
 - Es können mehrere Spieler gleichzeitig spielen.
 - Der Datenaustausch funktioniert über JSON-Strings und Java-Sockets.

### Technologien
 - Java
 - Java Swing
 - GSON
 - Maven

### Ressourcen
Spielkarten - https://www.kenney.nl/assets/playing-cards-pack
