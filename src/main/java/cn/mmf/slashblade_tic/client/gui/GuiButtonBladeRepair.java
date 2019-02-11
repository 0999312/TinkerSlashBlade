package cn.mmf.slashblade_tic.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.translation.I18n;

import slimeknights.tconstruct.library.client.Icons;
import slimeknights.tconstruct.tools.common.client.GuiButtonItem;

public class GuiButtonBladeRepair extends GuiButtonItem<SlashBladeBuildGuiInfo> {

  public static final SlashBladeBuildGuiInfo info;

  public GuiButtonBladeRepair(int buttonId, int x, int y) {
    super(buttonId, x, y, I18n.translateToLocal("gui.repair"), info);
  }

  @Override
  protected void drawIcon(Minecraft mc) {
    mc.getTextureManager().bindTexture(Icons.ICON);
    Icons.ICON_Anvil.draw(x, y);
  }

  static {
    int x = 7 + 80 / 2 - 8 - 6;
    int y = 18 + 64 / 2 - 8;

    info = new SlashBladeBuildGuiInfo();

    info.addSlotPosition(x, y);

    info.addSlotPosition(x - 18, y + 20); // -20,+20
    info.addSlotPosition(x - 22, y - 5); // -22, -7
    info.addSlotPosition(x, y - 23); // +-0, -21
    info.addSlotPosition(x + 22, y - 5); // +22, -7
    info.addSlotPosition(x + 18, y + 20); // +20,+20
  }
}