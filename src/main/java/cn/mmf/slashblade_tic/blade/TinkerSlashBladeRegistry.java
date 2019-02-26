package cn.mmf.slashblade_tic.blade;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.TLinkedHashSet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.TinkerAPIException;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.events.TinkerRegisterEvent;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.IPattern;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.Shard;
import slimeknights.tconstruct.library.tools.ToolCore;

public class TinkerSlashBladeRegistry {
	  // the logger for the library
	  public static final Logger log = Util.getLogger("API");  
	
	 /*---------------------------------------------------------------------------
	  | TOOLS & WEAPONS & Crafting                                                |
	  ---------------------------------------------------------------------------*/

	  /** This set contains all known tools */
	  private static final Set<SlashBladeCore> tools = new TLinkedHashSet<>();
	  private static final Set<IToolPart> toolParts = new TLinkedHashSet<>();
	  private static final Set<SlashBladeCore> toolStationCrafting = Sets.newLinkedHashSet();
	  private static final Set<SlashBladeCore> toolForgeCrafting = Sets.newLinkedHashSet();
	  private static final List<ItemStack> stencilTableCrafting = Lists.newLinkedList();
	  private static final Set<Item> patternItems = Sets.newHashSet();
	  private static final Set<Item> castItems = Sets.newHashSet();
	  private static Shard shardItem;

	  /**
	   * Register a tool, making it known to tinkers' systems.
	   * All toolparts used to craft the tool will be registered as well.
	   */
	  public static void registerTool(SlashBladeCore tool) {
	    tools.add(tool);

	    for(PartMaterialType pmt : tool.getRequiredComponents()) {
	      for(IToolPart tp : pmt.getPossibleParts()) {
	        registerToolPart(tp);
	      }
	    }
	  }

	  public static Set<SlashBladeCore> getTools() {
	    return ImmutableSet.copyOf(tools);
	  }

	  /**
	   * Used for the sharpening kit. Allows to register a toolpart that is not part of a tool.
	   */
	  public static void registerToolPart(IToolPart part) {
	    TinkerRegistry.registerToolPart(part);
	  }


	  /** Adds a tool to the Crafting UI of both the Tool Station as well as the Tool Forge */
	  public static void registerToolCrafting(SlashBladeCore tool) {
	    registerToolStationCrafting(tool);
	    registerToolForgeCrafting(tool);
	  }

	  /** Adds a tool to the Crafting UI of the Tool Station */
	  public static void registerToolStationCrafting(SlashBladeCore tool) {
	    toolStationCrafting.add(tool);
	  }

	  public static Set<SlashBladeCore> getToolStationCrafting() {
	    return ImmutableSet.copyOf(toolStationCrafting);
	  }

	  /** Adds a tool to the Crafting UI of the Tool Forge */
	  public static void registerToolForgeCrafting(SlashBladeCore tool) {
	    toolForgeCrafting.add(tool);
	  }

	  public static Set<SlashBladeCore> getToolForgeCrafting() {
	    return ImmutableSet.copyOf(toolForgeCrafting);
	  }

	  /** Adds a new pattern to craft to the stenciltable. NBT sensitive. Has to be a Pattern. */
	  public static void registerStencilTableCrafting(ItemStack stencil) {
	    if(!(stencil.getItem() instanceof IPattern)) {
	      error(String.format(
	          "Stencil Table Crafting has to be a pattern (%s)", stencil.toString()));
	      return;
	    }
	    stencilTableCrafting.add(stencil);
	  }

	  public static List<ItemStack> getStencilTableCrafting() {
	    return ImmutableList.copyOf(stencilTableCrafting);
	  }

	  public static void setShardItem(Shard shard) {
	    if(shard == null) {
	      return;
	    }
	    shardItem = shard;
	  }

	  public static Shard getShard() {
	    return shardItem;
	  }

	  public static ItemStack getShard(Material material) {
	    ItemStack out = material.getShard();
	    if(out.isEmpty()) {
	      out = shardItem.getItemstackWithMaterial(material);
	    }
	    return out;
	  }

	  /** Registers a pattern for the given item */
	  public static void addPatternForItem(Item item) {
	    patternItems.add(item);
	  }

	  /** Registers a cast for the given item */
	  public static void addCastForItem(Item item) {
	    castItems.add(item);
	  }

	  /** All items that have a pattern */
	  public static Collection<Item> getPatternItems() {
	    return ImmutableList.copyOf(patternItems);
	  }

	  /** All items that have a cast */
	  public static Collection<Item> getCastItems() {
	    return ImmutableList.copyOf(castItems);
	  }
	  
	  /*---------------------------------------------------------------------------
	  | Modifiers                                                                 |
	  ---------------------------------------------------------------------------*/
	  private static final Map<String, IModifier> modifiers = new THashMap<>();

	  public static void registerModifier(IModifier modifier) {
	    registerModifierAlias(modifier, modifier.getIdentifier());
	  }

	  /** Registers an alternate name for a modifier. This is used for multi-level modifiers/traits where multiple exist, but one specific is needed for access */
	  public static void registerModifierAlias(IModifier modifier, String alias) {
	    if(modifiers.containsKey(alias)) {
	      throw new TinkerAPIException("Trying to register a modifier with the name " + alias + " but it already is registered");
	    }
	    if(new TinkerRegisterEvent.ModifierRegisterEvent(modifier).fire()) {
	      modifiers.put(alias, modifier);
	    }
	    else {
	      log.debug("Registration of modifier " + alias + " has been cancelled by event");
	    }
	  }

	  public static IModifier getModifier(String identifier) {
	    return modifiers.get(identifier);
	  }

	  public static Collection<IModifier> getAllModifiers() {
	    return ImmutableList.copyOf(modifiers.values());
	  }	  
	  
	  private static void error(String message, Object... params) {
		    throw new TinkerAPIException(String.format(message, params));
	  }
}
