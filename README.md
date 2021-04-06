# `SEB Mariel Hajdari`

## `Git`
https://github.com/marielhajdari/SEB.git

## `Setup`
* DatabaseServer anpassen der Einträge: DB_URL, USER, PASS
* SQL Script in database.sql in einer PostgreSQL Database ausführen.
* Server wartet auf Requests.

## `Designs`

Die Architektur setzt sich Grundsätzlich aus 3 verschiedenen Bereichen zusammen, welche verschiedene Klassen beinhalten. (Packages sind mit Punkten dargestellt)

`database`
* DBconnection

`manage`
* History
* Tournament
* User

`server`
* CURLY - Enum
* ReadRequest
* RespondRequest

### `Database`
Die Datenbank besteht aus 4 Tabellen.
Users Tabelle mit allen Daten zu den Usern.
History Tabelle mit allen entrys der Usern( mit push up count, duration)
Tournament Tabelle mit alle laufende (aktiv) und nicht laufende(nicht aktiv) tournaments.
tourParticipants Tabelle mit den Infos aller Teilnehmer einer Tournament und ihre anzahl der pushups hinzugefügt.


SQL Statements zum Erstellen der Datenbank sind dem GitRepository beigefügt.
db_seb.sql

### `Manage`

#### `User`
Die User Klasse beinhaltet alle Informationen des Users und kann die Daten des aktuellen Users in der Datenbank Updaten. 

#### `History`
Die History Klasse hilft alle Informationen der Entrys in die Datebank zu speichern.

#### `Tournament`
Die Tournament Klasse started eine neue Tournament und speichert User Daten in die tourParticipant Tabelle der DB.

### `Server`

#### `CURLY`
Enum mit folgenden Attributen: GET, POST, PUT, DELETE, NOT_VALID

#### `ReadRequest`
Besitzt Variablen für den ankommenden Http Request und liest die ContentLength aus.

#### `RespondRequest`
Besitzt Variablen für die ausgehenden Http Response.


## `Failures and Selected Solutions`

Die zwei größten Fehlerquellen waren das fehlende Wissen über den Aufbau eines Rest Servers sowie nicht gezieltes Programmieren auf eine Implementierung einer Datenbankanbindung.

## `Unit Tests`






## `Time Tracking`

Die Zeit die für dieses Projekt reingesteckt wurde war doch hoch. Allerdings zum großen Teil an doch sehr viel Verwirrung wie genau alles aufgebaut gehört und anfangs einer anderen Implementation als schlussendlich verwendet wurde. Die Anbindung an die Datenbank hat viel geändert. 


Für SEB wurden ca. 55 Stunden gebraucht.