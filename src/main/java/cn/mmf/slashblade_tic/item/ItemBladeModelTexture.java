package cn.mmf.slashblade_tic.item;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cn.mmf.slashblade_tic.client.book.BookBladeSmith;
import mods.flammpfeil.slashblade.ItemSlashBladeNamed;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.TagPropertyAccessor;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.mantle.util.LocUtils;
import slimeknights.tconstruct.library.TinkerRegistry;

public class ItemBladeModelTexture extends Item {
	 public ItemBladeModelTexture() {
	        this.setCreativeTab(TinkerRegistry.tabParts);
	        this.setMaxStackSize(1);
	    }

	    @Override
	    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
	    	NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
	    	if(ItemSlashBladeNamed.CurrentItemName.exists(tag)){
	            tooltip.addAll(LocUtils.getTooltips(TextFormatting.GRAY.toString() + LocUtils.translateRecursive(
                "slashbladetic.blade_model.name")+":"+I18n.format("item."+ItemSlashBladeNamed.CurrentItemName.get(tag)+".name", new Object())));	
	    	}
	    	if(ItemSlashBlade.StandbyRenderType.exists(tag)){
	            tooltip.addAll(LocUtils.getTooltips(TextFormatting.GRAY.toString() + LocUtils.translateRecursive(
                "slashbladetic.blade_standby.name")+":"+getStandByString(tag)));	
	    	}
	    	if(ItemSlashBladeNamed.SummonedSwordColor.exists(tag)){
	            tooltip.addAll(LocUtils.getTooltips(TextFormatting.GRAY.toString() + LocUtils.translateRecursive(
                "slashbladetic.summonedswordcolor.name")+":0x"+Integer.toHexString(ItemSlashBladeNamed.SummonedSwordColor.get(tag))));	
	    	}
	    }
	    
	    private String getStandByString(NBTTagCompound tag) {
	    	if(ItemSlashBlade.StandbyRenderType.exists(tag)){
	    		switch (ItemSlashBlade.StandbyRenderType.get(tag)) {
				case 0:
					return I18n.format("slashblade.standby.null.name", new Object());
				case 1:
					return I18n.format("slashblade.standby.left_hand.name", new Object());
				case 2:
					return I18n.format("slashblade.standby.back.name", new Object());
				case 3:
					return I18n.format("slashblade.standby.ninja.name", new Object());
				default:
					break;
				}
	    	}
			return I18n.format("slashblade.standby.null.name", new Object());
		}
	    
	    private ItemStack getItemstackWithNBT(NBTTagCompound tag_paper2) {
			ItemStack paper = new ItemStack(RegisterLoader.blade_model_paper);
		    NBTTagCompound tag_result = ItemSlashBlade.getItemTagCompound(paper);
		    ItemSlashBlade.ModelName.set(tag_result,
		    		ItemSlashBlade.ModelName.exists(tag_paper2)? 
		    				ItemSlashBlade.ModelName.get(tag_paper2):"blade");
		    ItemSlashBlade.TextureName.set(tag_result,
		    		ItemSlashBlade.TextureName.exists(tag_paper2)?
		    				ItemSlashBlade.TextureName.get(tag_paper2):"blade");
		    ItemSlashBladeNamed.CurrentItemName.set(tag_result,
		    		ItemSlashBladeNamed.CurrentItemName.exists(tag_paper2)?
		    				ItemSlashBladeNamed.CurrentItemName.get(tag_paper2):"flammpfeil.slashblade.named");
		    ItemSlashBlade.StandbyRenderType.set(tag_result,
		    		ItemSlashBlade.StandbyRenderType.exists(tag_paper2)?
		    				ItemSlashBlade.StandbyRenderType.get(tag_paper2):0);
		    ItemSlashBladeNamed.SummonedSwordColor.set(tag_result,
		    		ItemSlashBladeNamed.SummonedSwordColor.exists(tag_paper2)?
		    				ItemSlashBladeNamed.SummonedSwordColor.get(tag_paper2):0x3333FF);
		    ItemSlashBladeNamed.SpecialAttackType.set(tag_result,
		    		ItemSlashBlade.SpecialAttackType.exists(tag_paper2)?
		    				ItemSlashBlade.SpecialAttackType.get(tag_paper2):0);
		    if(tag_paper2.hasKey("SB.SEffect"))
		    	tag_result.setTag("SB.SEffect",tag_paper2.getCompoundTag("SB.SEffect"));
		    return paper;
		  }
	    
	    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
	        if (!this.isInCreativeTab(tab)) return;
	        if (this.isInCreativeTab(CreativeTabs.COMBAT)) return;
	        for(String bladename : ItemSlashBladeNamed.NamedBlades){
	            ItemStack blade = SlashBlade.getCustomBlade(bladename);
	            NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(blade);
	           
	            if(!getItemstackWithNBT(tag).isEmpty()) subItems.add(getItemstackWithNBT(tag));
	        }
	    }
}
