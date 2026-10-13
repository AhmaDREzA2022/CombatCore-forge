package com.combatcore.mixin;

import com.combatcore.CombatCoreMod;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin {

	@Inject(
			method = "attackEntity(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/Entity;)V",
			at = @At("TAIL")
	)
	private void combatcore$onAttack(EntityPlayer player, Entity target, CallbackInfo ci) {
		CombatCoreMod.onAttack(target);
	}
}
