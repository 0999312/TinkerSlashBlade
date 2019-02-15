package cn.mmf.slashblade_tic.blade;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import slimeknights.tconstruct.library.events.TinkerToolEvent;
import slimeknights.tconstruct.library.events.TinkerEvent.OnItemBuilding;
import slimeknights.tconstruct.library.events.TinkerEvent.OnToolPartReplacement;
import slimeknights.tconstruct.library.events.TinkerToolEvent.OnRepair;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.TinkersItem;

/**
 * Base class for all tinkers events
 */
public abstract class TinkerSlashBladeEvent extends Event {

  /**
   * Fired when a tool is built.
   * This happens every time a tool is loaded as well as when the player actually builds the tool.
   * You can make changes to the tag compound and it'll land on the resulting tool, but its itemstack is not available.
   */
  public static class OnItemBuilding extends TinkerSlashBladeEvent {

    public NBTTagCompound tag;
    public final ImmutableList<Material> materials;
    public final SlashBladeTICBasic tool;

    public OnItemBuilding(NBTTagCompound tag, ImmutableList<Material> materials, SlashBladeTICBasic tool) {
      this.tag = tag;
      this.materials = materials;
      this.tool = tool;
    }

    public static OnItemBuilding fireEvent(NBTTagCompound tag, ImmutableList<Material> materials, SlashBladeTICBasic tool) {
      OnItemBuilding event = new OnItemBuilding(tag, materials, tool);
      MinecraftForge.EVENT_BUS.post(event);
      return event;
    }
  }

  /**
   * Fired when the player tries to replace a toolpart.
   * You can modify the input items to achieve different results, this will not modify the actual items in the game.
   * If you're modifying the list itself, make sure to put new items into originally empty indices to prevent the usage of other items in the input. Just append to the list.
   * You can not modify the tool that's getting modified
   */
  @Cancelable
  public static class OnToolPartReplacement extends TinkerSlashBladeEvent {

    /** The items in the tool station. Can be manipulated. */
    public NonNullList<ItemStack> replacementParts;
    public ItemStack toolStack;

    public OnToolPartReplacement(NonNullList<ItemStack> replacementParts, ItemStack toolStack) {
      this.replacementParts = replacementParts;
      this.toolStack = toolStack.copy();
    }

    public static boolean fireEvent(NonNullList<ItemStack> replacementParts, ItemStack toolStack) {
      return !MinecraftForge.EVENT_BUS.post(new OnToolPartReplacement(replacementParts, toolStack));
    }
  }
  

  public static class OnRepair extends TinkerSlashBladeEvent {

    public final int amount;

    public OnRepair(ItemStack itemStack, int amount) {
      this.amount = amount;
    }

    public static boolean fireEvent(ItemStack itemStack, int amount) {
      OnRepair event = new OnRepair(itemStack, amount);
      return !MinecraftForge.EVENT_BUS.post(event);
    }
  }
  
}