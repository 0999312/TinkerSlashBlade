package cn.mmf.slashblade_tic.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.mantle.util.LocUtils;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.ResourceLocationRaw;
import java.util.EnumSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.mmf.slashblade_tic.blade.SlashBladeCore;
import cn.mmf.slashblade_tic.blade.TinkerSlashBladeRegistry;

public class ItemSlashBladeSaya extends ItemSlashBlade implements IToolPart {

	public ItemSlashBladeSaya(ToolMaterial par2EnumToolMaterial){
        super(par2EnumToolMaterial, 0.0f);
    }

	@SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack arg0, World arg1, List arg2, ITooltipFlag arg3) {
      Material material = getMaterial(arg0);
      arg2.add(getModelTexture(arg0).toString());
      // Material traits/info
      boolean shift = Util.isShiftKeyDown();

      if(!checkMissingMaterialTooltip(arg0, arg2)) {
    	  arg2.addAll(getTooltipTraitInfo(material));
      }

      // Stats
      if(Config.extraTooltips) {
        if(!shift) {
          // info tooltip for detailed and component info
        	arg2.add("");
        	arg2.add(Util.translate("tooltip.tool.holdShift"));
        }
        else {
        	arg2.addAll(getTooltipStatsInfo(material));
        }
      }

      arg2.addAll(getAddedByInfo(material));
    }

  public boolean checkMissingMaterialTooltip(ItemStack stack, List<String> tooltip) {
	    return checkMissingMaterialTooltip(stack, tooltip, null);
	  }

	  public boolean checkMissingMaterialTooltip(ItemStack stack, List<String> tooltip, String statIdentifier) {
	    Material material = getMaterial(stack);

	    if(material == Material.UNKNOWN) {
	      NBTTagCompound tag = TagUtil.getTagSafe(stack);
	      String materialID = tag.getString(Tags.PART_MATERIAL);

	      String error;
	      if(!materialID.isEmpty()) {
	        error = I18n.translateToLocalFormatted("tooltip.part.missing_material", materialID);
	      }
	      else {
	        error = I18n.translateToLocal("tooltip.part.missing_info");
	      }
	      tooltip.addAll(LocUtils.getTooltips(error));
	      return true;
	    }
	    else if(statIdentifier != null && material.getStats(statIdentifier) == null) {
	      tooltip.addAll(LocUtils.getTooltips(Util.translateFormatted("tooltip.part.missing_stats", material.getLocalizedName(), statIdentifier)));
	      return true;
	    }

	    return false;
	  }
  
  public List<String> getTooltipTraitInfo(Material material) {
	    // We build a map with Stat -> Traits mappings that allows us to group or not group depending on what's available
	    Map<String, List<ITrait>> mapping = Maps.newConcurrentMap();

	    // go through all stats of the material, and check if they have a use, build the map from them
	    for(IMaterialStats stat : material.getAllStats()) {
	      if(hasUseForStat(stat.getIdentifier())) {
	        List<ITrait> traits = material.getAllTraitsForStats(stat.getIdentifier());
	        if(!traits.isEmpty()) {
	          boolean unified = false;
	          for(Map.Entry<String, List<ITrait>> entry : mapping.entrySet()) {
	            // group together if identical
	            if(entry.getValue().equals(traits)) {
	              mapping.put(entry.getKey() + ", " + stat.getLocalizedName(), entry.getValue());
	              mapping.remove(entry.getKey());
	              unified = true;
	              break;
	            }
	          }

	          if(!unified) {
	            mapping.put(stat.getLocalizedName(), traits);
	          }
	        }
	      }
	    }

	    List<String> tooltips = Lists.newLinkedList();
	    boolean withType = mapping.size() > 1;

	    // convert the entries into tooltips
	    for(Map.Entry<String, List<ITrait>> entry : mapping.entrySet()) {
	      // add the traits in "Stattype: Trait1, Trait2,..." style
	      StringBuilder sb = new StringBuilder();
	      if(withType) {
	        sb.append(TextFormatting.ITALIC.toString());
	        sb.append(entry.getKey());
	        sb.append(": ");
	        sb.append(TextFormatting.RESET.toString());
	      }
	      sb.append(material.getTextColor());
	      List<ITrait> traits = entry.getValue();
	      if(!traits.isEmpty()) {
	        ListIterator<ITrait> iter = traits.listIterator();

	        sb.append(iter.next().getLocalizedName());
	        while(iter.hasNext()) {
	          sb.append(", ").append(iter.next().getLocalizedName());
	        }

	        tooltips.add(sb.toString());
	      }
	    }

	    return tooltips;
	  }

	  public List<String> getTooltipStatsInfo(Material material) {
	    ImmutableList.Builder<String> builder = ImmutableList.builder();

	    for(IMaterialStats stat : material.getAllStats()) {
	      if(hasUseForStat(stat.getIdentifier())) {
	        List<String> text = stat.getLocalizedInfo();
	        if(!text.isEmpty()) {
	          builder.add("");
	          builder.add(TextFormatting.WHITE.toString() + TextFormatting.UNDERLINE + stat.getLocalizedName());
	          builder.addAll(stat.getLocalizedInfo());
	        }
	      }
	    }

	    return builder.build();
	  }

	  public List<String> getAddedByInfo(Material material) {
	    ImmutableList.Builder<String> builder = ImmutableList.builder();
	    if(TinkerRegistry.getTrace(material) != null) {
	      String materialInfo = I18n.translateToLocalFormatted("tooltip.part.material_added_by",
	                                                           TinkerRegistry.getTrace(material).getName());
	      builder.add("");
	      builder.add(materialInfo);
	    }
	    return builder.build();
	  }

	  @Nonnull
	  @Override
	  public String getItemStackDisplayName(@Nonnull ItemStack stack) {
	    Material material = getMaterial(stack);

	    String locString = getUnlocalizedName() + "." + material.getIdentifier();

	    // custom name?
	    if(I18n.canTranslate(locString)) {
	      return Util.translate(locString);
	    }

	    // no, create the default name combo
	    return material.getLocalizedItemName(super.getItemStackDisplayName(stack));
	  }
  
    @Override
    public ComboSequence getNextComboSeq(ItemStack itemStack, ComboSequence current, boolean isRightClick, EntityPlayer player) {
            return ComboSequence.None;
    }

    @Override
    public EnumSet<SwordType> getSwordType(ItemStack itemStack) {
            EnumSet<SwordType> set =  EnumSet.noneOf(SwordType.class);
            set.add(SwordType.Perfect);
            set.add(SwordType.Sealed);
            set.remove(SwordType.FiercerEdge);
            return set;
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
        ComboSequence comboSec = getComboSequence(getItemTagCompound(par1ItemStack));
        switch (comboSec) {
            case Saya1:
            case Saya2:
                break;

            default:
                break;
        }

        return super.hitEntity(par1ItemStack, par2EntityLivingBase, par3EntityLivingBase);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
            super.setDamage(stack,0);
    }
    @Override
    public String getMaterialID(ItemStack stack) {
      return getMaterial(stack).identifier;
    }

    @Override
    public Material getMaterial(ItemStack stack) {
      NBTTagCompound tag = TagUtil.getTagSafe(stack);

      return TinkerRegistry.getMaterial(tag.getString(Tags.PART_MATERIAL));
    }

    @Override
    public ItemStack getItemstackWithMaterial(Material material) {
      ItemStack stack = new ItemStack(this);
      NBTTagCompound tag = new NBTTagCompound();
      tag.setString(Tags.PART_MATERIAL, material.identifier);
      stack.setTagCompound(tag);
      ItemSlashBlade.TextureName.set(tag, "sheath/"+material.identifier+"_saya");
      return stack;
    }
    
    @Override
    public ResourceLocationRaw getModel() {
    	// TODO Auto-generated method stub
    	return new ResourceLocationRaw("flammpfeil.slashblade","model/blade_sheath.obj");
    }
    
	@Override
	public int getCost() {
		// TODO Auto-generated method stub
		return Material.VALUE_Ingot * 8;
	}
	
	  @Override
	  public boolean canUseMaterial(Material mat) {
	    for(SlashBladeCore tool : TinkerSlashBladeRegistry.getTools()) {
	      for(PartMaterialType pmt : tool.getRequiredComponents()) {
	        if(pmt.isValid(this, mat)) {
	          return true;
	        }
	      }
	    }

	    return false;
	  }

	  @Override
	  public boolean hasUseForStat(String stat) {
		  for(SlashBladeCore tool : TinkerSlashBladeRegistry.getTools()) {
	      for(PartMaterialType pmt : tool.getRequiredComponents()) {
	        if(pmt.isValidItem(this) && pmt.usesStat(stat)) {
	          return true;
	        }
	      }
	    }

	    return false;
	  }
	@Override
	public boolean canBeCasted() {
		// TODO Auto-generated method stub
		return true;
	}
  
	@Override
	public boolean canBeCrafted() {
		// TODO Auto-generated method stub
		return true;
	}
	
	  @Override
	  public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
	    if(this.isInCreativeTab(tab)) {
	      for(Material mat : TinkerRegistry.getAllMaterials()) {
	      
	        if(canUseMaterial(mat)) {
	          subItems.add(getItemstackWithMaterial(mat));
	          if(!Config.listAllMaterials) {
	            break;
	          }
	        }
	      }
	    }
	  }

}

