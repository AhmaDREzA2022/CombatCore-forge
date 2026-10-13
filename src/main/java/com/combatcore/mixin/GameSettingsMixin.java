package com.combatcore.mixin;

import com.combatcore.CombatCoreMod;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameSettings.class)
public class GameSettingsMixin {

	@Inject(method = "loadOptions()V", at = @At("HEAD"))
	private void combatcore$registerKeys(CallbackInfo ci) {
		CombatCoreMod.registerKeys((GameSettings) (Object) this);
	}
}
