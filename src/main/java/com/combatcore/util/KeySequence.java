package com.combatcore.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import java.util.function.BooleanSupplier;

public final class KeySequence {

	private static final KeyBinding[] NOTHING = new KeyBinding[0];

	private final BooleanSupplier startCheck;

	private int delay = -1;
	private int holdTicks;
	private int holdTicksLeft;

	private KeyBinding[] toPress = NOTHING;
	private KeyBinding[] toRelease = NOTHING;

	public KeySequence() {
		this(null);
	}

	public KeySequence(BooleanSupplier startCheck) {
		this.startCheck = startCheck;
	}

	public boolean isBusy() {
		return this.delay >= 0 || this.holdTicksLeft > 0;
	}

	public void queue(int delayTicks, int holdTicks, KeyBinding[] press, KeyBinding[] release) {
		this.delay = Math.max(0, delayTicks);
		this.holdTicks = Math.max(1, holdTicks);
		this.toPress = press == null ? NOTHING : press;
		this.toRelease = release == null ? NOTHING : release;
	}

	public void tick(Minecraft client) {
		if (this.holdTicksLeft > 0) {
			if (--this.holdTicksLeft == 0) {
				restoreAll();
			}
			return;
		}

		if (this.delay < 0) {
			return;
		}
		if (this.delay-- > 0) {
			return;
		}

		if (this.startCheck != null && !this.startCheck.getAsBoolean()) {
			return;
		}

		for (int i = 0; i < this.toPress.length; i++) {
			Keys.press(this, this.toPress[i]);
		}
		for (int i = 0; i < this.toRelease.length; i++) {
			Keys.set(this, this.toRelease[i], false);
		}
		this.holdTicksLeft = this.holdTicks;
	}

	public void cancel() {
		this.delay = -1;
		if (this.holdTicksLeft > 0) {
			this.holdTicksLeft = 0;
			restoreAll();
		}
	}

	private void restoreAll() {
		for (int i = 0; i < this.toPress.length; i++) {
			Keys.restore(this, this.toPress[i]);
		}
		for (int i = 0; i < this.toRelease.length; i++) {
			Keys.restore(this, this.toRelease[i]);
		}
	}
}
