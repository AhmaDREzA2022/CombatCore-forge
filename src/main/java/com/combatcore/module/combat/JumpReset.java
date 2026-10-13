package com.combatcore.module.combat;

import com.combatcore.module.Module;
import com.combatcore.module.setting.NumberSetting;
import com.combatcore.util.Keys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;

import java.util.Random;

public class JumpReset extends Module {

	private final NumberSetting chance = addSetting(new NumberSetting("Chance", 100, 0, 100, "%"));
	private final NumberSetting minHurtTime = addSetting(new NumberSetting("Min hurt time", 1, 1, 10, "t"));

	private final Random random = new Random();

	private int lastHurtTime;
	private int jumpTicksLeft;

	public JumpReset() {
		super("Jump Reset", "Combat", "Jumps when taking knockback to reduce KB");
	}

	@Override
	public void onTick(Minecraft client) {
		EntityPlayerSP player = client.thePlayer;
		if (player == null) return;

		int hurtTime = player.hurtTime;
		boolean freshHit = hurtTime > 0 && hurtTime > this.lastHurtTime;
		this.lastHurtTime = hurtTime;

		if (this.jumpTicksLeft > 0 && --this.jumpTicksLeft == 0) {
			Keys.restore(this, client.gameSettings.keyBindJump);
		}

		if (!freshHit || hurtTime < this.minHurtTime.getValue() || !player.onGround) {
			return;
		}

		if (this.random.nextInt(100) >= this.chance.getValue()) {
			return;
		}

		Keys.press(this, client.gameSettings.keyBindJump);
		this.jumpTicksLeft = 1;
	}

	@Override
	public void restoreKeys() {
		this.jumpTicksLeft = 0;
		Minecraft client = Minecraft.getMinecraft();
		if (client != null && client.gameSettings != null) {
			Keys.restore(this, client.gameSettings.keyBindJump);
		}
	}
}
