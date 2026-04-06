# RainChat Build Pipeline

Dieses Repository nutzt GitHub Actions für automatisierte Builds von:
- **Flatpak** (Linux)
- **Windows EXE**
- **macOS DMG**
- **JAR** (plattformübergreifend)

## Setup

### 1. Icon erstellen
Das Icon wird für die Windows EXE benötigt:

```bash
python3 create-icon.py
```

Dies erstellt `Rain.Network/icon.ico` (wird für jpackage benötigt).

### 2. Flatpak-Build-Skript vorbereiten
Das Skript `Rain.Network/build-flatpak.sh` muss ausführbar sein:

```bash
chmod +x Rain.Network/build-flatpak.sh
```

### 3. Automatische Builds

Die GitHub Action läuft automatisch bei:
- **Push zu master/main**: Erstellt Artifacts
- **Release-Tags** (z.B. `v1.0.0`): Erstellt GitHub Release mit allen Binaries

## Manuell bauen

### Lokal auf Linux (Flatpak):
```bash
cd Rain.Network
./build-flatpak.sh
# Erstellt: rainchat.flatpak
```

### Lokal auf Windows (EXE):
```cmd
cd Rain.Network
jpackage --input . --type exe --app-version 2.0 --name RainChat --main-jar Client.jar --main-class ch.nivisan.raincloud.network.client.Main --icon icon.ico
```

### Lokal auf macOS (DMG):
```bash
cd Rain.Network
# Icon zu ICNS konvertieren (optional)
python3 << 'EOF'
from PIL import Image
img = Image.open('icon.ico')
img.save('icon.icns')
EOF

jpackage --input . --type dmg --app-version 2.0 --name RainChat --main-jar Client.jar --main-class ch.nivisan.raincloud.network.client.Main --icon icon.icns
```
```bash
cd Rain.Network
find src -name '*.java' > /tmp/files.txt
javac @/tmp/files.txt -d bin
echo "Main-Class: ch.nivisan.raincloud.network.client.Main" > MANIFEST.MF
jar cfm Client.jar MANIFEST.MF -C bin .
```

## Release erstellen

1. Tag erstellen:
```bash
git tag v2.0.0
git push origin v2.0.0
```

2. GitHub Actions baut automatisch und erstellt ein Release mit:
   - `Client.jar`
   - `rainchat.flatpak` (Linux)
   - `RainChat-2.0.exe` (Windows)
   - `RainChat-2.0.dmg` (macOS)

## Troubleshooting

### Flatpak-Build schlägt fehl
- Stellen Sie sicher, dass `flatpak-builder` installiert ist
- OpenJDK 25 muss die richtige SHA256 haben (siehe `com.nivisan.raincloud.network.client.json`)

### Windows EXE funktioniert nicht
- Stellen Sie sicher, dass `icon.ico` existiert
- OpenJDK 25 muss auf dem Windows-Runner installiert sein (wird von GitHub automatisch gemacht)

### macOS DMG funktioniert nicht
- Stellen Sie sicher, dass PIL (Python Imaging Library) installiert ist zum Konvertieren von ICO zu ICNS
- Falls Icon-Konvertierung fehlschlägt, wird das DMG ohne Icon gebaut (funktioniert trotzdem)

## Logs ansehen

In GitHub:
1. Gehen Sie zum Tab **Actions**
2. Wählen Sie den Workflow **Build Flatpak and Windows EXE and macOS DMG**
3. Klicken Sie auf den Build und schauen Sie sich die Logs an
