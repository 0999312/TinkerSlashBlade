package cn.mmf.slashblade_tic.blade;

import java.util.List;

import cn.mmf.slashblade_tic.item.ItemLoader;
import mods.flammpfeil.slashblade.util.ResourceLocationRaw;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.tools.TinkerTools;

public class ItemSlashBladeTIC extends SlashBladeCore {
	
	 public static final float DURABILITY_MODIFIER = 1.1f;

	  public ItemSlashBladeTIC() {
	    super(PartMaterialType.handle(TinkerTools.toolRod),
	         PartMaterialType.head(TinkerTools.swordBlade),
	         PartMaterialType.extra(ItemLoader.wrapper));

	    addCategory(Category.WEAPON);
	  }

//	  @Override
//	public ResourceLocationRaw getModel() {
//		// TODO Auto-generated method stub
//		return new ResourceLocationRaw("flammpfeil.slashblade","model/blade_white.obj");
//	}
	  
	  @Override
	  public float damagePotential() {
	    return 1.0f;
	  }

	  @Override
	  public double attackSpeed() {
	    return -2d; // default vanilla sword speed
	  }
	
	  @Override
	  public float getRepairModifierForPart(int index) {
	    return DURABILITY_MODIFIER;
	  }

	  @Override
	  public ToolNBT buildTagData(List<Material> materials) {
	    ToolNBT data = buildDefaultTag(materials);
	    // 2 base damage, like vanilla swords
	    data.attack += 1f;
	    data.durability *= DURABILITY_MODIFIER;
	    return data;
	  }
}
