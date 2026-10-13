# Combat Core (Forge 1.8.9)

**Forge 1.8.9 port** of [CombatCore-Ornithe](https://github.com/AhmaDREzA2022/CombatCore-Ornithe).

Lightweight **client-side only** PvP helper for Minecraft 1.8.9.

Four modules — **W-Tap**, **S-Tap**, **Jump Reset** and **Block Hit** — plus a small module system, a ClickGUI and a JSON config.

No reach, aim, velocity, ESP or any other cheat: the mod only ever simulates input the player could produce themselves, through vanilla key bindings.

## Modules

All modules are in the *Combat* category and are toggleable via the ClickGUI or an optional per-module key binding.

### W-Tap
Releases the forward key for one tick shortly after landing a hit, then presses it again (classic sprint reset).

### S-Tap
Briefly presses S (backwards) after a hit while releasing W.

### Jump Reset
Jumps on the tick the player takes knockback to reduce horizontal KB.

### Block Hit
Right-clicks (blocks with the sword) a short moment after a hit.

## Controls

| Key | Action |
|-----|--------|
| Right Shift | Open / close the ClickGUI |
| Per-module (default unbound) | Toggle that module |

## Building

```bash
./gradlew setupDecompWorkspace
./gradlew build
```

Requires **JDK 8**. The jar appears in `build/libs/`.

## Port notes

- Original: Ornithe + Feather/Calamus mappings + Fabric entrypoints
- This port: ForgeGradle 2.1 + Sponge Mixin 0.7 + MCP `stable_22`
- KeyBinding simulation uses `KeyBinding.setKeyBindState`
- Mixins target `Minecraft.runTick`, `PlayerControllerMP.attackEntity`, `GameSettings.loadOptions`

Original license: CC0-1.0
