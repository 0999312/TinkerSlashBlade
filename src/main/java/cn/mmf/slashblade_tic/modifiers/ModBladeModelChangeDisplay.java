package cn.mmf.slashblade_tic.modifiers;

import com.google.common.collect.ImmutableList;

import cn.mmf.slashblade_tic.item.RegisterLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.IModifierDisplay;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.IToolPart;

/** A custom clientside modifier to handle the loading and displaying of the different fortify modifiers */
public class ModBladeModelChangeDisplay extends Modifier implements IModifierDisplay {

  public ModBladeModelChangeDisplay() {
    super(ModBladeModelChange.EXTRA_TRAIT_IDENTIFIER);
  }

  @Override
  public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
    // dummy
  }

  @Override
  public int getColor() {
    return 0x8B0000;
  }

  @Override
  public List<List<ItemStack>> getItems() {
    return RegisterLoader.sb.getRequiredComponents().stream()
                              .map(PartMaterialType::getPossibleParts)
                              .flatMap(Collection::stream)
                              .map(this::getItems)
                              .collect(Collectors.toList());
  }

  private List<ItemStack> getItems(IToolPart part) {
    
    return ImmutableList.<ItemStack>builder()
        .add(new ItemStack(RegisterLoader.blade_model_paper))
        .addAll(ModBladeModelChange.EMBOSSMENT_ITEMS)
        .build();
  }
}