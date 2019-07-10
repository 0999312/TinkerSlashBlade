package cn.mmf.slashblade_tic.compat.tinkersforging;

import javax.annotation.Nonnull;

import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.tinkersforging.common.recipe.AnvilRecipe;
import com.alcatrazescapee.tinkersforging.common.recipe.ModRecipes;
import com.alcatrazescapee.tinkersforging.util.ItemType;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeRule;
import com.alcatrazescapee.tinkersforging.util.material.MaterialRegistry;
import com.alcatrazescapee.tinkersforging.util.material.MaterialType;

import cn.mmf.slashblade_tic.Main;
import cn.mmf.slashblade_tic.blade.TinkerSlashBladeRegistry;
import cn.mmf.slashblade_tic.item.RegisterLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import static com.alcatrazescapee.alcatrazcore.util.OreDictionaryHelper.UPPER_UNDERSCORE_TO_LOWER_CAMEL;

public class TinkerForging_Recipes {

	public TinkerForging_Recipes() {
		for(Item item : TinkerSlashBladeRegistry.getAllPartItems()){
	        for (MaterialType material : MaterialRegistry.getAllMaterials())
	        {
	            if (!MaterialRegistry.isTinkersMaterial(material) || !material.isEnabled()) continue;
	
	            ItemStack output = getPartFor(item, material);
	            String inputOre = UPPER_UNDERSCORE_TO_LOWER_CAMEL.convert("INGOT_" + material.getName());
	
	            if (!output.isEmpty() && inputOre != null && OreDictionary.doesOreNameExist(inputOre))
	            	ModRecipes.ANVIL.add(new AnvilRecipe(output, inputOre, 4, material.getTier(), new ForgeRule[]{
	            			ForgeRule.DRAW_NOT_LAST,ForgeRule.HIT_LAST,ForgeRule.SHRINK_ANY
	            	}));
	            if (!output.isEmpty() &&output.getItem() == RegisterLoader.handle && inputOre != null && OreDictionary.doesOreNameExist(inputOre))
	            	ModRecipes.ANVIL.add(new AnvilRecipe(output, inputOre, 6, material.getTier(), new ForgeRule[]{
	            			ForgeRule.DRAW_NOT_LAST,ForgeRule.HIT_LAST,ForgeRule.SHRINK_ANY
	            	}));
	            if (!output.isEmpty() &&output.getItem() == RegisterLoader.wrapper && inputOre != null && OreDictionary.doesOreNameExist(inputOre))
	            	ModRecipes.ANVIL.add(new AnvilRecipe(output, inputOre, 5, material.getTier(), new ForgeRule[]{
	            			ForgeRule.BEND_NOT_LAST,ForgeRule.UPSET_LAST,ForgeRule.DRAW_ANY
	            	}));
	        }
		}
	}
	
	@Nonnull
    private static ItemStack getPartFor(Item item, MaterialType material)
    {
        final ItemStack stack = new ItemStack(item,1,0);
        if (!stack.isEmpty())
        {
            final NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("Material", material.getName());
            stack.setTagCompound(nbt);
        }
        return stack;
    }
}
