# Erstellen

```
$ docker build -t rain-network-server .

$ docker run -e PORT=8182 -p 8182:8182/udp rain-network-server
```

## Kommunikation im Protokoll

Der Server und Client kommunizieren per UDP mit einfachen Paket-Tags:

- `/c/<name>/<pubkey>/e/` : Client-Handshake (sendet Benutzername + RSA-PublicKey URL-sicher)
- `/ks/<payload>/e/` : Server-Antwort mit symmetrischem Schlüssel, RSA-verschlüsselt und URL-safe kodiert
- `/c/<id>/e/` : Server bestätigt Verbindungs-ID
- `/e/<base64>/e/` : verschlüsselte Anwendungspayload (AES-CFB8 + Standard Base64)
- `/m/<text>/e/` : Chat-Nachricht (klartext, wenn kein Handshake)
- `/u/` : Nutzerliste anfordern
- `/i/<id>/e/` : Keepalive-Ping/Pong
- `/d/<id>/e/` : Trennen

### Ablauf beim Client-Login

1. Client generiert RSA KeyPair.
2. Client sendet `/c/<name>/<pubkey>/e/` an den Server.
3. Server erstellt neuen Client-Eintrag mit ID und generiert AES-Schlüssel + IV.
4. Server verschlüsselt den symmetrischen Token `SYMMETRIC:<id>:<key>:<iv>` mit RSA PublicKey des Clients.
5. Server sendet `/ks/<urlsafe-base64-rsa>/e/`.
6. Client entschlüsselt mit eigenem RSA PrivateKey.
7. Client speichert AES-Schlüssel + IV und markiert Handshake abgeschlossen.
8. Server sendet `/c/<id>/e/` zur Bestätigung.

## Verschlüsselung

- RSA (3072 Bit) wird nur zur Schlüsselaushandlung verwendet.
- Symmetrische Nachrichtencodierung nutzt AES/CFB8/NoPadding.
- Verschlüsselte Chat-Payload wird mit Standard-Base64 (`Base64.getEncoder()/getDecoder()`) kodiert.
- RSA-Keys und `SYMMETRIC:` Token im Handshake werden URL-safe Base64 kodiert (`Base64.getUrlEncoder().withoutPadding()`).

## Nachrichten-Lesbarkeit

- Nach vollständigem Handshake (`/c/<id>/e/` + `/ks/`) werden weitere Pakete wie `/e/<base64>/e/` entschlüsselt und dann intern auf `/m/`, `/u/`, `/i/`, `/d/` geprüft.
- Keepalive: Server schickt `/i/server/e/`, Client antwortet `/i/<id>/e/`.

## Fehlervermeidung

- Prüft Paketgrenzen (`lastIndexOf("/e/")`) bevor Kodierung/Entschlüsselung.
- Gültigkeitsprüfungen verhindern Crash durch falsche Base64-Strings.
