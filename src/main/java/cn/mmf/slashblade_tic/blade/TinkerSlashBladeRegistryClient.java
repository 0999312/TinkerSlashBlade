package cn.mmf.slashblade_tic.blade;

import com.google.common.collect.Maps;

import cn.mmf.slashblade_tic.client.gui.SlashBladeBuildGuiInfo;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Logger;

import java.util.Map;

import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.client.texture.AbstractColoredTexture;

@SideOnly(Side.CLIENT)
public final class TinkerSlashBladeRegistryClient {

  // the logger for the library
  public static final Logger log = Util.getLogger("API-Client");

  private TinkerSlashBladeRegistryClient() {
  }

  /*---------------------------------------------------------------------------
  | GUI & CRAFTING                                                            |
  ---------------------------------------------------------------------------*/
  private static final Map<Item, SlashBladeBuildGuiInfo> toolBuildInfo = Maps.newLinkedHashMap();

  public static void addToolBuilding(SlashBladeBuildGuiInfo info) {
    toolBuildInfo.put(info.tool.getItem(), info);
  }

  public static SlashBladeBuildGuiInfo getToolBuildInfoForTool(Item tool) {
    return toolBuildInfo.get(tool);
  }

  public static void clear() {
    toolBuildInfo.clear();
  }

  /*---------------------------------------------------------------------------
  | MATERIAL TEXTURE CREATION                                                 |
  ---------------------------------------------------------------------------*/
  private static final Map<String, AbstractColoredTexture> textureProcessors = Maps.newHashMap();

}