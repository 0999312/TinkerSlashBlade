package cn.mmf.slashblade_tic.modifiers;

import com.google.common.collect.ImmutableList;

import cn.mmf.slashblade_tic.blade.SlashBladeCore;
import cn.mmf.slashblade_tic.blade.TinkerSlashBladeRegistry;
import cn.mmf.slashblade_tic.item.RegisterLoader;
import cn.mmf.slashblade_tic.util.SlashBladeBuilder;
import mods.flammpfeil.slashblade.ItemSlashBladeNamed;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.modifiers.TinkerGuiException;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.tools.modifiers.ToolModifier;

public class ModBladeModelChange extends ToolModifier {

  public static final List<ItemStack> EMBOSSMENT_ITEMS = getEmbossmentItems();
  public static final String EXTRA_TRAIT_IDENTIFIER = "blade_model_change";
  public final Set<SlashBladeCore> toolCores;
  private final ItemStack blade;
  private final NBTTagCompound tag_paper;

  public ModBladeModelChange(ItemStack blade_model, NBTTagCompound tag_model) {
    this(blade_model,tag_model, ItemSlashBladeNamed.CurrentItemName.get(tag_model));
  }

  public ModBladeModelChange(ItemStack blade_model, NBTTagCompound tag_model,String id) {
      super(EXTRA_TRAIT_IDENTIFIER + id, 0X8B0000);

    TinkerSlashBladeRegistry.registerModifier(this);
    this.tag_paper = tag_model;
    this.toolCores = new HashSet<>();
    this.blade = blade_model;
    addAspects(new ExtraTraitAspect(), new ModifierAspect.SingleAspect(this), new ModifierAspect.DataAspect(this));
  }

  public void addCombination(SlashBladeCore toolCore) {
    toolCores.add(toolCore);
    ItemStack toolPartItem = getItemstackWithNBT(tag_paper);
    List<ItemStack> stacks = new ArrayList<>();
    stacks.add(toolPartItem);
    stacks.addAll(EMBOSSMENT_ITEMS);
    addRecipeMatch(new RecipeMatch.ItemCombination(1, stacks.toArray(new ItemStack[stacks.size()])));
  }

  private ItemStack getItemstackWithNBT(NBTTagCompound tag_paper2) {
	ItemStack paper = new ItemStack(RegisterLoader.blade_model_paper);
    NBTTagCompound tag_result = ItemSlashBlade.getItemTagCompound(paper);
    ItemSlashBlade.ModelName.set(tag_result,ItemSlashBlade.ModelName.exists(tag_paper2)? ItemSlashBlade.ModelName.get(tag_paper2):"blade");
    ItemSlashBlade.TextureName.set(tag_result, ItemSlashBlade.TextureName.exists(tag_paper2)?ItemSlashBlade.TextureName.get(tag_paper2):"blade");
    ItemSlashBladeNamed.CurrentItemName.set(tag_result, ItemSlashBladeNamed.CurrentItemName.exists(tag_paper2)?ItemSlashBladeNamed.CurrentItemName.get(tag_paper2):"flammpfeil.slashblade.named");
	return paper;
  }



  @Override
  public boolean canApplyCustom(ItemStack stack) throws TinkerGuiException {
    return stack.getItem() instanceof SlashBladeCore && toolCores.contains(stack.getItem());
  }

  @Override
  public String getLocalizedName() {
    return Util.translate(LOC_Name, EXTRA_TRAIT_IDENTIFIER) + " (" + I18n.format("item."+
  (ItemSlashBladeNamed.CurrentItemName.exists(tag_paper)
    		?ItemSlashBladeNamed.CurrentItemName.get(tag_paper)
    				:"flammpfeil.slashblade.named")+".name", new Object()) + ")";
  }

  @Override
  public String getLocalizedDesc() {
    return Util.translateFormatted(String.format(LOC_Desc, EXTRA_TRAIT_IDENTIFIER), 
    		I18n.format("item."+
    (ItemSlashBladeNamed.CurrentItemName.exists(tag_paper)
    		?ItemSlashBladeNamed.CurrentItemName.get(tag_paper)
    				:"flammpfeil.slashblade.named")+".name", new Object()));
  }

  @Override
  public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
	  ItemSlashBlade.ModelName.set(rootCompound,ItemSlashBlade.ModelName.exists(tag_paper)? ItemSlashBlade.ModelName.get(tag_paper):"blade");
	    ItemSlashBlade.TextureName.set(rootCompound, ItemSlashBlade.TextureName.exists(tag_paper)?ItemSlashBlade.TextureName.get(tag_paper):"blade");
	    ItemSlashBladeNamed.CurrentItemName.set(rootCompound, ItemSlashBladeNamed.CurrentItemName.exists(tag_paper)?ItemSlashBladeNamed.CurrentItemName.get(tag_paper):"flammpfeil.slashblade.named");
  }

  @Override
  public boolean hasTexturePerMaterial() {
    return true;
  }

  private static class ExtraTraitAspect extends ModifierAspect {

    @Override
    public boolean canApply(ItemStack stack, ItemStack original) throws TinkerGuiException {
      NBTTagList modifierList = TagUtil.getModifiersTagList(original);
      for(int i = 0; i < modifierList.tagCount(); i++) {
        NBTTagCompound tag = modifierList.getCompoundTagAt(i);
        ModifierNBT data = ModifierNBT.readTag(tag);
        if(data.identifier.startsWith(EXTRA_TRAIT_IDENTIFIER)) {
          throw new TinkerGuiException(Util.translate("gui.error.already_has_model"));
        }
      }
      return true;
    }

    @Override
    public void updateNBT(NBTTagCompound root, NBTTagCompound modifierTag) {
      // nothing to do
    }


  }

  private static List<ItemStack> getEmbossmentItems() {
    ItemStack green = TinkerCommons.matSlimeCrystalGreen;
    ItemStack blue = TinkerCommons.matSlimeCrystalBlue;
    ItemStack red = TinkerCommons.matSlimeCrystalMagma;
    ItemStack expensive = new ItemStack(Items.PAPER);

    if(green == null) {
      green = new ItemStack(Items.SLIME_BALL);
    }
    if(blue == null) {
      blue = new ItemStack(Items.SLIME_BALL);
    }
    if(red == null) {
      red = new ItemStack(Items.SLIME_BALL);
    }

    return ImmutableList.of(green, blue, red, expensive);
  }
}