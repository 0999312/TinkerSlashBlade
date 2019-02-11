package cn.mmf.slashblade_tic.client.gui;

import com.google.common.collect.Lists;
import cn.mmf.slashblade_tic.blade.SlashBladeTICBasic;
import net.minecraft.item.ItemStack;
import org.lwjgl.util.Point;
import java.util.List;
import javax.annotation.Nonnull;

public class SlashBladeBuildGuiInfo {

  @Nonnull
  public final ItemStack tool;
  // the positions where the slots are located
  public final List<Point> positions = Lists.newArrayList();

  public SlashBladeBuildGuiInfo() {
    // for repairing
    this.tool = ItemStack.EMPTY;
  }

  public SlashBladeBuildGuiInfo(@Nonnull SlashBladeTICBasic tool) {
    this.tool = tool.buildItemForRenderingInGui();
  }

  public static SlashBladeBuildGuiInfo default3Part(@Nonnull SlashBladeTICBasic tool) {
    SlashBladeBuildGuiInfo info = new SlashBladeBuildGuiInfo(tool);
    info.addSlotPosition(33 - 20, 42 + 20);
    info.addSlotPosition(33 + 20, 42 - 20);
    info.addSlotPosition(33, 42);
    return info;
  }

  /**
   * Add another slot at the specified position for the tool.
   * The positions are usually located between:
   * X: 7 - 69
   * Y: 18 - 64
   */
  public void addSlotPosition(int x, int y) {
    positions.add(new Point(x, y));
  }

}