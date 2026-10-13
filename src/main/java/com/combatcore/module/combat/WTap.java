package com.combatcore.module.combat;

import com.combatcore.module.Module;
import com.combatcore.module.setting.NumberSetting;
import com.combatcore.util.KeySequence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;

import java.util.Random;

public class WTap extends Module {

	private static final int RELEASE_TICKS = 1;

	private final NumberSetting delay = addSetting(new NumberSetting("Delay", 0, 0, 10, "t"));
	private final NumberSetting chance = addSetting(new NumberSetting("Chance", 100, 0, 100, "%"));

	private final Random random = new Random();
	private final KeySequence sequence = new KeySequence();

	public WTap() {
		super("W-Tap", "Combat", "Releases and re-presses W after a hit to reset sprint");
	}

	@Override
	public void onAttack(Entity target) {
		if (this.sequence.isBusy()) {
			return;
		}

		Minecraft client = Minecraft.getMinecraft();
		if (client == null || client.thePlayer == null || client.gameSettings == null
				|| !client.thePlayer.isSprinting()) {
			return;
		}

		if (this.random.nextInt(100) >= this.chance.getValue()) {
			return;
		}

		this.sequence.queue(this.delay.getValue(), RELEASE_TICKS,
				null, new KeyBinding[]{client.gameSettings.keyBindForward});
	}

	@Override
	public void onTick(Minecraft client) {
		this.sequence.tick(client);
	}

	@Override
	public void restoreKeys() {
		this.sequence.cancel();
	}
}
