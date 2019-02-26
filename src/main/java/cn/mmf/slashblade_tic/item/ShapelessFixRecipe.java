package cn.mmf.slashblade_tic.item;

import cn.mmf.slashblade_tic.blade.SlashBladeCore;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessFixRecipe extends ShapelessOreRecipe {

	private ItemStack item1;
	public ShapelessFixRecipe(ResourceLocation group, ItemStack result, ItemStack item) {
		super(group, result, new Object[]{
				item,item
		});
		item1=item;
	}
	@Override
	public boolean matches(InventoryCrafting inv, World world) {
	     boolean result = super.matches(inv, world);

	        if(result && !item1.isEmpty()){
	        	item1.setItemDamage(OreDictionary.WILDCARD_VALUE);
	            for(int idx = 0; idx < inv.getSizeInventory(); idx++){
	                ItemStack curIs = inv.getStackInSlot(idx);
	                if(!curIs.isEmpty()
	                        && curIs.getItem() instanceof SlashBladeCore
	                        && curIs.hasTagCompound()){
	                	return true;
	                	}
	                }
	            }
		return false;
	}
}
