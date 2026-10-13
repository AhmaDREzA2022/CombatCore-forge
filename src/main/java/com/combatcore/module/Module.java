package com.combatcore.module;

import com.combatcore.CombatCoreMod;
import com.combatcore.module.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Module {

	private final String name;
	private final String category;
	private final String description;
	private final List<Setting<?>> settings = new ArrayList<Setting<?>>();
	private final KeyBinding keyBinding;

	private boolean enabled;

	protected Module(String name, String category, String description) {
		this.name = name;
		this.category = category;
		this.description = description;
		this.keyBinding = new KeyBinding("Toggle " + name, 0, CombatCoreMod.KEY_CATEGORY);
	}

	protected <S extends Setting<?>> S addSetting(S setting) {
		this.settings.add(setting);
		return setting;
	}

	public String getName() {
		return this.name;
	}

	public String getCategory() {
		return this.category;
	}

	public String getDescription() {
		return this.description;
	}

	public List<Setting<?>> getSettings() {
		return Collections.unmodifiableList(this.settings);
	}

	public KeyBinding getKeyBinding() {
		return this.keyBinding;
	}

	public int getKeyCode() {
		return this.keyBinding.getKeyCode();
	}

	public void setKeyCode(int keyCode) {
		if (this.keyBinding.getKeyCode() == keyCode) {
			return;
		}
		this.keyBinding.setKeyCode(keyCode);
		KeyBinding.resetKeyBindingArrayAndHash();
	}

	public boolean consumeClick() {
		return this.keyBinding.isPressed();
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void toggle() {
		this.setEnabled(!this.enabled);
	}

	public void setEnabled(boolean enabled) {
		if (this.enabled == enabled) {
			return;
		}
		this.enabled = enabled;
		if (enabled) {
			this.onEnable();
		} else {
			this.onDisable();
		}
	}

	public void onEnable() {
	}

	public void onDisable() {
		this.restoreKeys();
	}

	public void onTick(Minecraft client) {
	}

	public void onAttack(Entity target) {
	}

	public void restoreKeys() {
	}
}
