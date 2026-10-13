package com.combatcore.module.combat;

import com.combatcore.module.Module;
import com.combatcore.module.setting.NumberSetting;
import com.combatcore.util.KeySequence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class BlockHit extends Module {

	private final NumberSetting delay = addSetting(new NumberSetting("Delay", 0, 0, 10, "t"));
	private final NumberSetting hold = addSetting(new NumberSetting("Hold", 2, 1, 4, "t"));

	private final KeySequence sequence = new KeySequence(this::pressAllowed);

	public BlockHit() {
		super("Block Hit", "Combat", "Right-clicks with the sword shortly after a hit");
	}

	@Override
	public void onAttack(Entity target) {
		if (this.sequence.isBusy()) {
			return;
		}

		Minecraft client = Minecraft.getMinecraft();
		if (client == null || client.thePlayer == null || client.gameSettings == null) {
			return;
		}

		ItemStack stack = client.thePlayer.getCurrentEquippedItem();
		if (stack == null || !(stack.getItem() instanceof ItemSword)) {
			return;
		}

		this.sequence.queue(this.delay.getValue(), this.hold.getValue(),
				new KeyBinding[]{client.gameSettings.keyBindUseItem}, null);
	}

	@Override
	public void onTick(Minecraft client) {
		this.sequence.tick(client);
	}

	@Override
	public void restoreKeys() {
		this.sequence.cancel();
	}

	private boolean pressAllowed() {
		Minecraft client = Minecraft.getMinecraft();
		return client == null || client.thePlayer == null || !client.thePlayer.isUsingItem();
	}
}
