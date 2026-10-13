# Combat Core (Forge 1.8.9)

**Forge 1.8.9 port** of [CombatCore-Ornithe](https://github.com/AhmaDREzA2022/CombatCore-Ornithe).

Lightweight **client-side only** PvP helper for Minecraft 1.8.9.

Four modules — **W-Tap**, **S-Tap**, **Jump Reset** and **Block Hit** — plus a module system, ClickGUI and JSON config.

No reach, aim, velocity, ESP or any other cheat: the mod only simulates input the player could produce themselves through vanilla key bindings.

## Requirements

- **JDK 8** (important – newer JDKs often break old ForgeGradle)
- Git
- Internet (to download Gradle + Minecraft/Forge dependencies the first time)

## Build from Terminal (no IntelliJ needed)

```bash
git clone https://github.com/AhmaDREzA2022/CombatCore-forge.git
cd CombatCore-forge

# Make the wrapper executable (Linux / macOS)
chmod +x gradlew

# First time only – downloads Gradle 4.9 + all dependencies (can take several minutes)
./gradlew setupDecompWorkspace

# Build the mod
./gradlew build
```

On **Windows** use:

```bat
gradlew.bat setupDecompWorkspace
gradlew.bat build
```

The finished jar will be in:

```
build/libs/CombatCore-1.0.0.jar
```

Copy that jar into your Minecraft `mods` folder (Forge 1.8.9).

### If `gradle-wrapper.jar` is missing

The binary jar cannot always be committed cleanly. If you get an error about the wrapper jar, run this once (requires a system Gradle or download it manually):

```bash
# Option A – if you already have any Gradle installed
gradle wrapper --gradle-version 4.9

# Option B – download the jar yourself
mkdir -p gradle/wrapper
# Then place gradle-wrapper.jar into gradle/wrapper/
# You can get it from any other Forge 1.8.9 project or from Gradle distributions.
```

After that `./gradlew` will work normally.

## Modules

| Module      | Description                                      |
|-------------|--------------------------------------------------|
| W-Tap       | Releases + re-presses W after a hit (sprint reset) |
| S-Tap       | Briefly presses S after a hit                    |
| Jump Reset  | Jumps on knockback to reduce KB                  |
| Block Hit   | Right-clicks with sword shortly after a hit      |

## Controls

- **Right Shift** – open / close ClickGUI
- Per-module keys (default unbound) – toggle individual modules

Config is saved to `.minecraft/config/combatcore.json`.

## License

Original project is CC0-1.0. This port keeps the same spirit.
