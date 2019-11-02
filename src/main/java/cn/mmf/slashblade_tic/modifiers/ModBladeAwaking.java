package cn.mmf.slashblade_tic.modifiers;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.modifiers.TinkerGuiException;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.tools.modifiers.ToolModifier;

public class ModBladeAwaking extends ToolModifier {
  public ModBladeAwaking() {
    super("blade_awaking", 0x8B008B);
    addAspects(new AwakingAspect(),
    		new ModifierAspect.SingleAspect(this), 
    		new ModifierAspect.DataAspect(this));
  }
  
  @Override
  public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
	  ItemSlashBlade.SpecialAttackType.set(rootCompound,
	    		ModBladeModelChange.SA_TYPES_HIDDEN.exists(rootCompound)?
	    				ModBladeModelChange.SA_TYPES_HIDDEN.get(rootCompound):0);
	    if(rootCompound.hasKey("SB.SEffect.hidden"))
	    	rootCompound.setTag("SB.SEffect",rootCompound.getCompoundTag("SB.SEffect.hidden"));
  }

  private static class AwakingAspect extends ModifierAspect {

    @Override
    public boolean canApply(ItemStack stack, ItemStack original) throws TinkerGuiException {
      NBTTagList modifierList = TagUtil.getModifiersTagList(original);
      boolean flag = false;
      for(int i = 0; i < modifierList.tagCount(); i++) {
        NBTTagCompound tag = modifierList.getCompoundTagAt(i);
        ModifierNBT data = ModifierNBT.readTag(tag);
        if(data.identifier.startsWith("blade_model_change")) {
        	flag=true;
        }
      }
      if(!flag) throw new TinkerGuiException(Util.translate("gui.error.no_blade_awaking"));
      return true;
    }

    @Override
    public void updateNBT(NBTTagCompound root, NBTTagCompound modifierTag) {
      // nothing to do
    }


  }
}