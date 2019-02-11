package cn.mmf.slashblade_tic.container;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.StringUtils;
import net.minecraft.world.WorldServer;
import slimeknights.mantle.inventory.BaseContainer;
import slimeknights.mantle.util.ItemStackList;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.common.TinkerNetwork;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.events.TinkerCraftingEvent;
import slimeknights.tconstruct.library.modifiers.TinkerGuiException;
import slimeknights.tconstruct.library.tinkering.IModifyable;
import slimeknights.tconstruct.library.tinkering.IRepairable;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.tools.common.inventory.ContainerTinkerStation;

import java.util.List;
import java.util.Set;

import cn.mmf.slashblade_tic.blade.SlashBladeCore;
import cn.mmf.slashblade_tic.blade.SlashBladeTICBasic;
import cn.mmf.slashblade_tic.blade.TinkerSlashBladeRegistry;
import cn.mmf.slashblade_tic.block.tileentity.TileBladeStation;
import cn.mmf.slashblade_tic.client.gui.GuiBladeStation;
import cn.mmf.slashblade_tic.packet.BladeStationSelectionPacket;
import cn.mmf.slashblade_tic.packet.BladeStationTextPacket;
import cn.mmf.slashblade_tic.util.SlashBladeBuilder;

// also tool forge
public class ContainerBladeStation extends ContainerTinkerStation<TileBladeStation> {

  private final EntityPlayer player;
  protected SlotBladeStationOut out;
  protected SlashBladeCore selectedTool; // needed for newly opened containers to sync
  protected int activeSlots;
  public String toolName;

  public ContainerBladeStation(InventoryPlayer playerInventory, TileBladeStation tile) {
    super(tile);
    this.player = playerInventory.player;

    // input slots
    int i;
    for(i = 0; i < tile.getSizeInventory(); i++) {
      addSlotToContainer(new SlotBladeStationIn(tile, i, 0, 0, this));
    }

    // output slot
    out = new SlotBladeStationOut(i, 124, 38, this);
    addSlotToContainer(out);
    this.addPlayerInventory(playerInventory, 8, 84 + 8);
    onCraftMatrixChanged(playerInventory);
  }

  public ItemStack getResult() {
    return out.getStack();
  }

  @Override
  protected void syncNewContainer(EntityPlayerMP player) {
    this.activeSlots = tile.getSizeInventory();
    TinkerNetwork.sendTo(new BladeStationSelectionPacket(null, tile.getSizeInventory()), player);
  }

  @Override
  protected void syncWithOtherContainer(BaseContainer<TileBladeStation> otherContainer, EntityPlayerMP player) {
    this.syncWithOtherContainer((ContainerBladeStation) otherContainer, player);
  }

  protected void syncWithOtherContainer(ContainerBladeStation otherContainer, EntityPlayerMP player) {
    // set same selection as other container
    this.setToolSelection(otherContainer.selectedTool, otherContainer.activeSlots);
    this.setToolName(otherContainer.toolName);
    // also send the data to the player
    TinkerNetwork.sendTo(new BladeStationSelectionPacket(otherContainer.selectedTool, otherContainer.activeSlots), player);
    if(otherContainer.toolName != null && !otherContainer.toolName.isEmpty()) {
      TinkerNetwork.sendTo(new BladeStationTextPacket(otherContainer.toolName), player);
    }
  }

  public void setToolSelection(SlashBladeCore tool, int activeSlots) {
    if(activeSlots > tile.getSizeInventory()) {
      activeSlots = tile.getSizeInventory();
    }

    this.activeSlots = activeSlots;
    this.selectedTool = tool;

    for(int i = 0; i < tile.getSizeInventory(); i++) {
      Slot slot = inventorySlots.get(i);
      // set part info for the slot
      if(slot instanceof SlotBladeStationIn) {
    	  SlotBladeStationIn slotToolPart = (SlotBladeStationIn) slot;

        slotToolPart.setRestriction(null);

        // deactivate not needed slots
        if(i >= activeSlots) {
          slotToolPart.deactivate();
        }
        // activate the other slots and set toolpart if possible
        else {
          slotToolPart.activate();
          if(tool != null) {
            List<PartMaterialType> pmts = tool.getToolBuildComponents();
            if(i < pmts.size()) {
              slotToolPart.setRestriction(pmts.get(i));
            }
          }
        }

        if(world.isRemote) {
          slotToolPart.updateIcon();
        }
      }
    }
  }

  public void setToolName(String name) {
    this.toolName = name;

    if(world.isRemote) {
      GuiScreen screen = Minecraft.getMinecraft().currentScreen;
      if(screen instanceof GuiBladeStation) {
        ((GuiBladeStation) screen).textField.setText(name);
      }
    }

    onCraftMatrixChanged(tile);
    if(out.getHasStack()) {
      if(name != null && !name.isEmpty()) {
        out.inventory.getStackInSlot(0).setStackDisplayName(name);
      }
      else {
        out.inventory.getStackInSlot(0).clearCustomName();
      }
    }
  }

  // update crafting - called whenever the content of an input slot changes
  @Override
  public void onCraftMatrixChanged(IInventory inventoryIn) {
    // reset gui state
    updateGUI();
    try {
      ItemStack result;
      // 1. try repairing
      result = repairTool(false);
      // 2. try swapping tool parts
      if(result.isEmpty()) {
        result = replaceToolParts(false);
      }
      // 3. try modifying
      if(result.isEmpty()) {
        result = modifyTool(false);
      }
      // 4. try renaming
      if(result.isEmpty()) {
        result = renameTool();
      }
      // 5. try building a new tool
      if(result.isEmpty()) {
        result = buildTool();
      }

      out.inventory.setInventorySlotContents(0, result);
      updateGUI();
    } catch(TinkerGuiException e) {
      // error ;(
      out.inventory.setInventorySlotContents(0, ItemStack.EMPTY);
      this.error(e.getMessage());
    }
    // sync output with other open containers on the server
    if(!this.world.isRemote) {
      WorldServer server = (WorldServer) this.world;
      for(EntityPlayer player : server.playerEntities) {
        if(player.openContainer != this && player.openContainer instanceof ContainerBladeStation && this.sameGui((ContainerBladeStation) player.openContainer)) {
          ((ContainerBladeStation) player.openContainer).out.inventory.setInventorySlotContents(0, out.getStack());
        }
      }
    }
  }

  // Called when the crafting result is taken out of its slot
  public void onResultTaken(EntityPlayer playerIn, ItemStack stack) {
    boolean resultTaken = false;

    try {
      resultTaken = !repairTool(true).isEmpty() ||
                    !replaceToolParts(true).isEmpty() ||
                    !modifyTool(true).isEmpty() ||
                    !renameTool().isEmpty();
    } catch(TinkerGuiException e) {
      // no error updating needed
      e.printStackTrace();
    }

    if(resultTaken) {
      updateSlotsAfterToolAction();
    }
    else {
      // calculate the result again (serverside)
      try {
        ItemStack tool = buildTool();

        // we built a tool
        if(!tool.isEmpty()) {
          // remove 1 of each in the slots
          // it's guaranteed that each slot that has an item has used exactly 1 item to build the tool
          for(int i = 0; i < tile.getSizeInventory(); i++) {
            tile.decrStackSize(i, 1);
          }

          setToolName("");
        }
      } catch(TinkerGuiException e) {
        // no error updating needed
        e.printStackTrace();
      }
    }
    onCraftMatrixChanged(null);

    this.playCraftSound(playerIn);
  }

  protected void playCraftSound(EntityPlayer player) {
    Sounds.playSoundForAll(player, Sounds.saw, 0.8f, 0.8f + 0.4f * TConstruct.random.nextFloat());
  }

  private ItemStack repairTool(boolean remove) {
    ItemStack repairable = getToolStack();

    // modifying possible?
    if(repairable.isEmpty() || !(repairable.getItem() instanceof IRepairable)) {
      return ItemStack.EMPTY;
    }

    return SlashBladeBuilder.tryRepairTool(getInputs(), repairable, remove);
  }

  private ItemStack replaceToolParts(boolean remove) throws TinkerGuiException {
    ItemStack tool = getToolStack();

    if(tool.isEmpty() || !(tool.getItem() instanceof SlashBladeTICBasic)) {
      return ItemStack.EMPTY;
    }

    NonNullList<ItemStack> inputs = getInputs();
    ItemStack result = SlashBladeBuilder.tryReplaceToolParts(tool, inputs, remove);
    if(!result.isEmpty()) {
      TinkerCraftingEvent.ToolPartReplaceEvent.fireEvent(result, player, inputs);
    }
    return result;
  }

  private ItemStack modifyTool(boolean remove) throws TinkerGuiException {
    ItemStack modifyable = getToolStack();

    // modifying possible?
    if(modifyable.isEmpty() || !(modifyable.getItem() instanceof IModifyable)) {
      return ItemStack.EMPTY;
    }

    ItemStack result = SlashBladeBuilder.tryModifyTool(getInputs(), modifyable, remove);
    if(!result.isEmpty()) {
      TinkerCraftingEvent.ToolModifyEvent.fireEvent(result, player, modifyable.copy());
    }
    return result;
  }

  private ItemStack renameTool() throws TinkerGuiException {
    ItemStack tool = getToolStack();

    // modifying possible?
    if(tool.isEmpty() ||
       !(tool.getItem() instanceof SlashBladeTICBasic) ||
       StringUtils.isNullOrEmpty(toolName) ||
       tool.getDisplayName().equals(toolName)) {
      return ItemStack.EMPTY;
    }

    ItemStack result = tool.copy();
    if(TagUtil.getNoRenameFlag(result)) {
      throw new TinkerGuiException(Util.translate("gui.error.no_rename"));
    }

    result.setStackDisplayName(toolName);

    return result;
  }

  private ItemStack buildTool() throws TinkerGuiException {
    NonNullList<ItemStack> input = ItemStackList.withSize(tile.getSizeInventory());
    for(int i = 0; i < input.size(); i++) {
      input.set(i, tile.getStackInSlot(i));
    }

    ItemStack result = SlashBladeBuilder.tryBuildTool(input, toolName, getBuildableTools());
    if(!result.isEmpty()) {
      TinkerCraftingEvent.ToolCraftingEvent.fireEvent(result, player, input);
    }
    return result;
  }

  protected Set<SlashBladeCore> getBuildableTools() {
    return TinkerSlashBladeRegistry.getToolStationCrafting();
  }

  private ItemStack getToolStack() {
    return inventorySlots.get(0).getStack();
  }

  /**
   * Removes the tool in the input slot and fixes all stacks that have stacksize 0 after being used up.
   */
  private void updateSlotsAfterToolAction() {
// perfect, items already got removed but we still have to clean up 0-stacks and remove the tool
    tile.setInventorySlotContents(0, ItemStack.EMPTY); // slot where the tool was
    for(int i = 1; i < tile.getSizeInventory(); i++) {
      if(!tile.getStackInSlot(i).isEmpty() && tile.getStackInSlot(i).getCount() == 0) {
        tile.setInventorySlotContents(i, ItemStack.EMPTY);
      }
    }
  }

  private NonNullList<ItemStack> getInputs() {
    NonNullList<ItemStack> input = NonNullList.withSize(tile.getSizeInventory() - 1, ItemStack.EMPTY);
    for(int i = 1; i < tile.getSizeInventory(); i++) {
      input.set(i - 1, tile.getStackInSlot(i));
    }

    return input;
  }

  @Override
  public boolean canMergeSlot(ItemStack stack, Slot slot) {
    return slot != out && super.canMergeSlot(stack, slot);
  }


}