package com.combatcore;

import com.combatcore.config.ConfigManager;
import com.combatcore.gui.ClickGuiScreen;
import com.combatcore.module.Module;
import com.combatcore.module.ModuleManager;
import com.combatcore.util.Keys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = CombatCoreMod.MODID, name = "Combat Core", version = "1.0.0", clientSideOnly = true)
@SideOnly(Side.CLIENT)
public class CombatCoreMod {

    public static final String MODID = "combatcore";
    public static final String KEY_CATEGORY = "Combat Core";

    public static final Logger LOGGER = LogManager.getLogger("Combat Core");

    public static final ModuleManager MODULES = new ModuleManager();

    public static final KeyBinding GUI_KEY =
            new KeyBinding("Open Combat Core ClickGUI", Keyboard.KEY_RSHIFT, KEY_CATEGORY);

    private static final int GUI_COOLDOWN_TICKS = 2;
    private static int guiCooldown;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientRegistry.registerKeyBinding(GUI_KEY);
        for (Module module : MODULES.getModules()) {
            ClientRegistry.registerKeyBinding(module.getKeyBinding());
        }

        MinecraftForge.EVENT_BUS.register(this);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                ConfigManager.saveNow();
            }
        }, "Combat Core config saver"));

        LOGGER.info("Loaded - {} modules, ClickGUI key: {}",
                MODULES.getModules().size(), Keys.getName(GUI_KEY.getKeyCode()));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        Minecraft client = Minecraft.getMinecraft();
        if (client == null) return;
        onClientTickInternal(client);
    }

    public static void onClientTickInternal(Minecraft client) {
        if (client.gameSettings == null) {
            return;
        }

        ConfigManager.ensureLoaded(client);

        boolean guiClick = GUI_KEY.isPressed();
        if (guiClick) {
            if (client.currentScreen == null && guiCooldown == 0) {
                client.displayGuiScreen(new ClickGuiScreen());
            }
            guiCooldown = GUI_COOLDOWN_TICKS;
        } else if (guiCooldown > 0) {
            guiCooldown--;
        }

        MODULES.tick(client);
        ConfigManager.tick();
    }

    public static void suppressGuiOpen() {
        guiCooldown = GUI_COOLDOWN_TICKS;
    }

    public static void registerKeys(GameSettings options) {
        // Keys are registered via ClientRegistry; this is kept for mixin compatibility
    }

    public static List<KeyBinding> allKeys() {
        List<KeyBinding> keys = new ArrayList<KeyBinding>();
        keys.add(GUI_KEY);
        for (Module module : MODULES.getModules()) {
            keys.add(module.getKeyBinding());
        }
        return keys;
    }

    public static void onAttack(Entity target) {
        MODULES.onAttack(target);
    }
}
