package com.combatcore.gui;

import com.combatcore.CombatCoreMod;
import com.combatcore.config.ConfigManager;
import com.combatcore.module.Module;
import com.combatcore.module.setting.NumberSetting;
import com.combatcore.module.setting.Setting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;

/**
 * Simple ClickGUI for Combat Core (Forge 1.8.9 port).
 * Left-click toggles modules, right-click expands settings, drag sliders.
 */
public class ClickGuiScreen extends GuiScreen {

	private static final int PANEL_X = 20;
	private static final int PANEL_Y = 20;
	private static final int PANEL_WIDTH = 140;
	private static final int MODULE_HEIGHT = 14;
	private static final int SETTING_HEIGHT = 12;

	private Module expanded;
	private NumberSetting dragging;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();

		List<Module> modules = CombatCoreMod.MODULES.getModules();
		int y = PANEL_Y;

		drawRect(PANEL_X, PANEL_Y - 14, PANEL_X + PANEL_WIDTH, PANEL_Y, 0xCC000000);
		this.fontRendererObj.drawStringWithShadow("Combat Core", PANEL_X + 4, PANEL_Y - 11, 0xFFFFFF);

		for (Module module : modules) {
			boolean hovered = mouseX >= PANEL_X && mouseX <= PANEL_X + PANEL_WIDTH
					&& mouseY >= y && mouseY < y + MODULE_HEIGHT;

			int bg = module.isEnabled() ? 0xAA2E7D32 : (hovered ? 0xAA424242 : 0xAA212121);
			drawRect(PANEL_X, y, PANEL_X + PANEL_WIDTH, y + MODULE_HEIGHT, bg);
			this.fontRendererObj.drawStringWithShadow(module.getName(), PANEL_X + 4, y + 3,
					module.isEnabled() ? 0x55FF55 : 0xFFFFFF);

			y += MODULE_HEIGHT;

			if (module == this.expanded) {
				for (Setting<?> setting : module.getSettings()) {
					if (setting instanceof NumberSetting) {
						NumberSetting ns = (NumberSetting) setting;
						drawRect(PANEL_X + 2, y, PANEL_X + PANEL_WIDTH - 2, y + SETTING_HEIGHT, 0xAA000000);
						this.fontRendererObj.drawStringWithShadow(ns.getDisplayString(), PANEL_X + 6, y + 2, 0xCCCCCC);

						int barX = PANEL_X + 6;
						int barW = PANEL_WIDTH - 16;
						int barY = y + SETTING_HEIGHT - 4;
						drawRect(barX, barY, barX + barW, barY + 2, 0xFF555555);
						int fill = (int) (ns.getFraction() * barW);
						drawRect(barX, barY, barX + fill, barY + 2, 0xFF4CAF50);

						y += SETTING_HEIGHT;
					}
				}
			}
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		List<Module> modules = CombatCoreMod.MODULES.getModules();
		int y = PANEL_Y;

		for (Module module : modules) {
			if (mouseX >= PANEL_X && mouseX <= PANEL_X + PANEL_WIDTH
					&& mouseY >= y && mouseY < y + MODULE_HEIGHT) {
				if (mouseButton == 0) {
					module.toggle();
					ConfigManager.markDirty();
				} else if (mouseButton == 1) {
					this.expanded = (this.expanded == module) ? null : module;
				}
				return;
			}
			y += MODULE_HEIGHT;

			if (module == this.expanded) {
				for (Setting<?> setting : module.getSettings()) {
					if (setting instanceof NumberSetting) {
						NumberSetting ns = (NumberSetting) setting;
						if (mouseX >= PANEL_X && mouseX <= PANEL_X + PANEL_WIDTH
								&& mouseY >= y && mouseY < y + SETTING_HEIGHT) {
							this.dragging = ns;
							updateSlider(ns, mouseX);
							return;
						}
						y += SETTING_HEIGHT;
					}
				}
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (this.dragging != null) {
			updateSlider(this.dragging, mouseX);
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		this.dragging = null;
		super.mouseReleased(mouseX, mouseY, state);
	}

	private void updateSlider(NumberSetting ns, int mouseX) {
		int barX = PANEL_X + 6;
		int barW = PANEL_WIDTH - 16;
		float frac = (mouseX - barX) / (float) barW;
		ns.setValue(ns.valueAtFraction(frac));
		ConfigManager.markDirty();
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE || keyCode == CombatCoreMod.GUI_KEY.getKeyCode()) {
			ConfigManager.saveNow();
			CombatCoreMod.suppressGuiOpen();
			this.mc.displayGuiScreen(null);
			return;
		}
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void onGuiClosed() {
		ConfigManager.saveNow();
	}
}
