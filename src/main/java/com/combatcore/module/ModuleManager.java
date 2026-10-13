package com.combatcore.module;

import com.combatcore.config.ConfigManager;
import com.combatcore.module.combat.BlockHit;
import com.combatcore.module.combat.JumpReset;
import com.combatcore.module.combat.STap;
import com.combatcore.module.combat.WTap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ModuleManager {

	private final List<Module> modules = new ArrayList<Module>();
	private boolean screenWasOpen;

	public ModuleManager() {
		this.modules.add(new WTap());
		this.modules.add(new STap());
		this.modules.add(new JumpReset());
		this.modules.add(new BlockHit());
	}

	public List<Module> getModules() {
		return Collections.unmodifiableList(this.modules);
	}

	public Module get(String name) {
		for (Module module : this.modules) {
			if (module.getName().equalsIgnoreCase(name)) {
				return module;
			}
		}
		return null;
	}

	public void tick(Minecraft client) {
		if (client.gameSettings == null) {
			return;
		}

		boolean screenOpen = client.currentScreen != null;

		if (screenOpen && !this.screenWasOpen) {
			this.restoreEnabled(client);
		}
		this.screenWasOpen = screenOpen;

		boolean inGame = client.thePlayer != null && client.theWorld != null;

		for (Module module : this.modules) {
			boolean pressed = module.consumeClick();

			if (pressed && !screenOpen) {
				module.toggle();
				ConfigManager.markDirty();
				this.sendToggleMessage(client, module);
			}

			if (module.isEnabled() && !screenOpen && inGame) {
				module.onTick(client);
			}
		}
	}

	public void onAttack(Entity target) {
		Minecraft client = Minecraft.getMinecraft();
		if (client == null || client.currentScreen != null || client.thePlayer == null) {
			return;
		}

		if (target == null || target == client.thePlayer || !(target instanceof EntityLivingBase)) {
			return;
		}

		for (Module module : this.modules) {
			if (module.isEnabled()) {
				module.onAttack(target);
			}
		}
	}

	public void restoreEnabled(Minecraft client) {
		for (Module module : this.modules) {
			if (module.isEnabled()) {
				module.restoreKeys();
			}
		}
	}

	private void sendToggleMessage(Minecraft client, Module module) {
		if (client.thePlayer == null) {
			return;
		}
		client.thePlayer.addChatMessage(new ChatComponentText("\u00a78[\u00a77Combat Core\u00a78] \u00a7r" + module.getName()
				+ ": " + (module.isEnabled() ? "\u00a7aON" : "\u00a7cOFF")));
	}
}
