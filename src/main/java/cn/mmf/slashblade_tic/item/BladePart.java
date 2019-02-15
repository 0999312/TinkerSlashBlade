package cn.mmf.slashblade_tic.item;

import cn.mmf.slashblade_tic.blade.SlashBladeCore;
import cn.mmf.slashblade_tic.blade.TinkerSlashBladeRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.tools.ToolPart;

public class BladePart extends ToolPart {

	public BladePart(int cost) {
		super(cost);
		// TODO Auto-generated constructor stub
	}

	  @Override
	  public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
	    if(this.isInCreativeTab(tab)) {
	      for(Material mat : TinkerRegistry.getAllMaterials()) {
	        // check if the material makes sense for this item (is it usable to build stuff?)
	        if(canUseMaterial(mat)) {
	          subItems.add(getItemstackWithMaterial(mat));
	          if(!Config.listAllMaterials) {
	            break;
	          }
	        }
	      }
	    }
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
}
