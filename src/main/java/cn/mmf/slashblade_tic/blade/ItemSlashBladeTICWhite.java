package cn.mmf.slashblade_tic.blade;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import cn.mmf.slashblade_tic.item.RegisterLoader;
import cn.mmf.slashblade_tic.util.TextureMixer;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.ResourceLocationRaw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.tools.TinkerTools;

public class ItemSlashBladeTICWhite extends SlashBladeCore {
	
	public static final float DURABILITY_MODIFIER = 1.1f;

	  public ItemSlashBladeTICWhite() {
	    super(PartMaterialType.handle(TinkerTools.toughToolRod),
	         PartMaterialType.head(RegisterLoader.blade),
	         PartMaterialType.extra(RegisterLoader.wrapper));

	    addCategory(Category.WEAPON);
	  }

		private final Cache<List<Material>, ResourceLocationRaw> texCache = CacheBuilder.newBuilder()
				.maximumSize(500L)
				.expireAfterWrite(10L, TimeUnit.MINUTES)
				.build();  
		
		@Override
		public EnumSet<SwordType> getSwordType(ItemStack itemStack) {
			EnumSet<SwordType> set = super.getSwordType(itemStack);
			set.remove(SwordType.Enchanted);
			set.remove(SwordType.Bewitched);
			set.remove(SwordType.SoulEeater);
			return set;
		}

		@Override
	    public void addInformationSwordClass(ItemStack par1ItemStack,
				EntityPlayer par2EntityPlayer, List par3List, boolean par4) {}
		  @Override
			public List<ResourceLocationRaw> getMuitlModelTexture() {
				List<ResourceLocationRaw> res = new ArrayList<ResourceLocationRaw>();
				res.add(new ResourceLocationRaw("flammpfeil.slashblade","model/handle/handle_white.png"));
				res.add(new ResourceLocationRaw("flammpfeil.slashblade","model/blade/blade.png"));
				res.add(new ResourceLocationRaw("flammpfeil.slashblade","model/sheath/saya_white.png"));
				res.add(new ResourceLocationRaw("flammpfeil.slashblade","model/handle/handle_color.png"));
				res.add(new ResourceLocationRaw("flammpfeil.slashblade","model/blade/blade_color.png"));
				res.add(new ResourceLocationRaw("flammpfeil.slashblade","model/sheath/saya_color.png"));
				return res;
			}
			
	@SideOnly(Side.CLIENT)
	@Override
	public ResourceLocationRaw getModelTexture(ItemStack par1ItemStack){
		TextureMixer texture_mixer = TextureMixer.getInstance();
		List<ResourceLocationRaw> list_model = getMuitlModelTexture();
		List<Material> materials = TinkerUtil.getMaterialsFromTagList(TagUtil.getBaseMaterialsTagList(par1ItemStack));

		ResourceLocationRaw res = new ResourceLocationRaw("flammpfeil.slashblade","model/blade.png");
		try {
			res = texCache.get(materials, () -> texture_mixer.generateTexture(
					texture_mixer.TextureMix(
							texture_mixer.Rendering("slashblade_white",materials, list_model), 1.0F)
					)
				);
		} catch (ExecutionException e) {
		}
				
		return res;
	}

	  @Override
	public ResourceLocationRaw getModel() {
		// TODO Auto-generated method stub
		return new ResourceLocationRaw("flammpfeil.slashblade","model/blade_white.obj");
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
	    data.attack += 1.5f;
	    data.durability *= DURABILITY_MODIFIER;
	    return data;
	  }
	  
}
