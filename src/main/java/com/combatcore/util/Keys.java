package com.combatcore.util;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public final class Keys {

	private static final Map<KeyBinding, Boolean> PRE_FORCE = new IdentityHashMap<KeyBinding, Boolean>();
	private static final Map<KeyBinding, Boolean> FORCED = new IdentityHashMap<KeyBinding, Boolean>();
	private static final Map<KeyBinding, Set<Object>> OWNERS =
			new IdentityHashMap<KeyBinding, Set<Object>>();

	private Keys() {
	}

	public static void set(Object owner, KeyBinding binding, boolean pressed) {
		if (owner == null || binding == null || binding.getKeyCode() == 0) {
			return;
		}

		Set<Object> owners = OWNERS.get(binding);
		if (owners == null) {
			owners = Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
			OWNERS.put(binding, owners);
		}
		if (owners.isEmpty()) {
			PRE_FORCE.put(binding, Boolean.valueOf(binding.isKeyDown()));
		}

		owners.add(owner);
		FORCED.put(binding, Boolean.valueOf(pressed));
		KeyBinding.setKeyBindState(binding.getKeyCode(), pressed);
	}

	public static void press(Object owner, KeyBinding binding) {
		set(owner, binding, true);
	}

	public static void restore(Object owner, KeyBinding binding) {
		if (owner == null || binding == null) {
			return;
		}

		Set<Object> owners = OWNERS.get(binding);
		if (owners == null || !owners.remove(owner)) {
			return;
		}

		if (!owners.isEmpty()) {
			return;
		}

		Boolean pre = PRE_FORCE.remove(binding);
		Boolean forcedTo = FORCED.remove(binding);
		OWNERS.remove(binding);

		if (pre == null || forcedTo == null) {
			return;
		}

		if (Boolean.valueOf(binding.isKeyDown()).equals(forcedTo)) {
			KeyBinding.setKeyBindState(binding.getKeyCode(), pre.booleanValue());
		}
	}

	public static String getName(int keyCode) {
		return keyCode <= 0 ? "None" : Keyboard.getKeyName(keyCode);
	}
}
