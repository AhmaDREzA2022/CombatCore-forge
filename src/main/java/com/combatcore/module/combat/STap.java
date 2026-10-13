package com.combatcore.module.combat;

import com.combatcore.module.Module;
import com.combatcore.module.setting.NumberSetting;
import com.combatcore.util.KeySequence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;

public class STap extends Module {

	private final NumberSetting delay = addSetting(new NumberSetting("Delay", 0, 0, 10, "t"));
	private final NumberSetting duration = addSetting(new NumberSetting("Duration", 1, 1, 10, "t"));

	private final KeySequence sequence = new KeySequence();

	public STap() {
		super("S-Tap", "Combat", "Briefly presses S after a hit to reset sprint");
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

		this.sequence.queue(this.delay.getValue(), this.duration.getValue(),
				new KeyBinding[]{client.gameSettings.keyBindBack},
				new KeyBinding[]{client.gameSettings.keyBindForward});
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
