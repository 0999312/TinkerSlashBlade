package cn.mmf.slashblade_tic.blade;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import cn.mmf.slashblade_tic.item.ItemLoader;
import cn.mmf.slashblade_tic.util.TextureMixer;
import mods.flammpfeil.slashblade.client.model.BladeModel;
import mods.flammpfeil.slashblade.client.model.BladeModelManager;
import mods.flammpfeil.slashblade.util.ResourceLocationRaw;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.tools.TinkerTools;

public class ItemSlashBladeTIC extends SlashBladeCore {
	
	public static final float DURABILITY_MODIFIER = 1.1f;

	  public ItemSlashBladeTIC() {
	    super(PartMaterialType.handle(TinkerTools.toolRod),
	         PartMaterialType.head(TinkerTools.swordBlade),
	         PartMaterialType.extra(ItemLoader.wrapper));

	    addCategory(Category.WEAPON);
	  }

	@Override
	public ResourceLocationRaw getModelTexture(ItemStack par1ItemStack){
		List<ResourceLocationRaw> list = getMuitlModelTexture(par1ItemStack);
		BufferedImage image = null;
		ResourceLocationRaw res = new ResourceLocationRaw("flammpfeil.slashblade","model/blade.png");
		try {
			image = TextureMixer.TextureMix(list, 1.0F);
			res = TextureMixer.generateTexture(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return res;
	}

	  
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
