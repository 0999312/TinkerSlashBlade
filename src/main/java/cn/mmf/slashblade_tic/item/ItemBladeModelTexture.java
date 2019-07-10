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
	    
	    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
	        if (!this.isInCreativeTab(tab)) return;
	        if (this.isInCreativeTab(CreativeTabs.COMBAT)) return;
	        for(String bladename : ItemSlashBladeNamed.NamedBlades){
	            ItemStack blade = SlashBlade.getCustomBlade(bladename);
	            NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(blade);
	            ItemStack result = new ItemStack(RegisterLoader.blade_model_paper);
	            NBTTagCompound tag_result = ItemSlashBlade.getItemTagCompound(result);
	            ItemSlashBlade.ModelName.set(tag_result,ItemSlashBlade.ModelName.exists(tag)? ItemSlashBlade.ModelName.get(tag):"blade");
	  			ItemSlashBlade.TextureName.set(tag_result, ItemSlashBlade.TextureName.exists(tag)?ItemSlashBlade.TextureName.get(tag):"blade");
	  			ItemSlashBladeNamed.CurrentItemName.set(tag_result, ItemSlashBladeNamed.CurrentItemName.exists(tag)?ItemSlashBladeNamed.CurrentItemName.get(tag):"flammpfeil.slashblade.named");
	  			ItemSlashBlade.StandbyRenderType.set(tag_result, ItemSlashBlade.StandbyRenderType.exists(tag)?ItemSlashBlade.StandbyRenderType.get(tag):0);
	  			ItemSlashBladeNamed.SummonedSwordColor.set(tag_result, ItemSlashBladeNamed.SummonedSwordColor.exists(tag)?ItemSlashBladeNamed.SummonedSwordColor.get(tag):0x3333FF);
	            if(!result.isEmpty()) subItems.add(result);
	        }
	    }
}
