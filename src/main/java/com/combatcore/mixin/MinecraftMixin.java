package com.combatcore.mixin;

import com.combatcore.CombatCoreMod;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Inject(method = "runTick()V", at = @At("HEAD"))
	private void combatcore$onTick(CallbackInfo ci) {
		CombatCoreMod.onClientTickInternal((Minecraft) (Object) this);
	}
}
