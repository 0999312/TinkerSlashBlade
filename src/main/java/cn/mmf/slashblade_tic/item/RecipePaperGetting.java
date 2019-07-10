package cn.mmf.slashblade_tic.item;

import mods.flammpfeil.slashblade.ItemSlashBladeNamed;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

import cn.mmf.slashblade_tic.Main;

public class RecipePaperGetting extends ShapedOreRecipe
{

    public RecipePaperGetting()
    {
        super(new ResourceLocation(Main.MODID,"recipe_model"), new ItemStack(SlashBlade.weapon, 1, 0),
                "X ",
                "B ",
                'X',"paper",
                'B',new ItemStack(SlashBlade.weapon, 1, 0));
    }

    public static boolean containsMatch(boolean strict, List<ItemStack> inputs, ItemStack... targets)
    {
        for (ItemStack input : inputs)
            for (ItemStack target : targets)
                if (OreDictionary.itemMatches(target, input, strict))
                    return true;
        return false;
    }

    @Override
    public boolean matches(InventoryCrafting cInv, World par2World)
    {
        	boolean hasBlade = false;
        	boolean hasGrindstone = false;
        	ItemStack stone = cInv.getStackInRowAndColumn(0, 0);
            if(stone.isEmpty())
                return false;
            List<ItemStack> ores = OreDictionary.getOres("paper");
            hasGrindstone = containsMatch(false,ores,stone);

        	if(hasGrindstone){
	            ItemStack target = cInv.getStackInRowAndColumn(0, 1);
	            if(!target.isEmpty() && target.getItem() instanceof ItemSlashBlade)
	            				hasBlade = true;
        	}
            return hasBlade && hasGrindstone;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting cInv)
    {
        ItemStack target = cInv.getStackInRowAndColumn(0, 1);

        ItemStack itemstack = target.copy();

        return itemstack;
    }

	@SubscribeEvent
	public void onCrafting(PlayerEvent.ItemCraftedEvent event){
        ItemStack item = event.crafting;
		IInventory craftMatrix = event.craftMatrix;
		if(craftMatrix instanceof InventoryCrafting){
			InventoryCrafting craftMatrix1 = (InventoryCrafting) craftMatrix;
			IRecipe recipe = ForgeRegistries.RECIPES.getValue(new ResourceLocation(Main.MODID, "recipe_model"));
			if(recipe!=null){
			if(!item.isEmpty()&&recipe.matches(craftMatrix1, event.player.world)){
	        if(item.getItem() instanceof ItemSlashBlade){
	        	if(item.hasTagCompound()){
	        		NBTTagCompound tag = item.getTagCompound();
	        		ItemStack new_paper=getItemstackWithNBT(tag);
	        		
	    			if (!event.player.inventory.addItemStackToInventory(new_paper))
	    				event.player.dropItem(new_paper, false);
	        		}
	        	}
				}
			}
		}
	}
	  private ItemStack getItemstackWithNBT(NBTTagCompound tag_paper2) {
			ItemStack paper = new ItemStack(RegisterLoader.blade_model_paper);
		    NBTTagCompound tag_result = ItemSlashBlade.getItemTagCompound(paper);
		    ItemSlashBlade.ModelName.set(tag_result,ItemSlashBlade.ModelName.exists(tag_paper2)? ItemSlashBlade.ModelName.get(tag_paper2):"blade");
		    ItemSlashBlade.TextureName.set(tag_result, ItemSlashBlade.TextureName.exists(tag_paper2)?ItemSlashBlade.TextureName.get(tag_paper2):"blade");
		    ItemSlashBladeNamed.CurrentItemName.set(tag_result, ItemSlashBladeNamed.CurrentItemName.exists(tag_paper2)?ItemSlashBladeNamed.CurrentItemName.get(tag_paper2):"flammpfeil.slashblade.named");
		    ItemSlashBlade.StandbyRenderType.set(tag_result, ItemSlashBlade.StandbyRenderType.exists(tag_paper2)?ItemSlashBlade.StandbyRenderType.get(tag_paper2):0);
		    ItemSlashBladeNamed.SummonedSwordColor.set(tag_result, ItemSlashBladeNamed.SummonedSwordColor.exists(tag_paper2)?ItemSlashBladeNamed.SummonedSwordColor.get(tag_paper2):0x3333FF);
			return paper;
		  }

}

