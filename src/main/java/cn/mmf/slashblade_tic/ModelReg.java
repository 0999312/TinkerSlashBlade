package cn.mmf.slashblade_tic;

import cn.mmf.slashblade_tic.client.model.BladeSpecialRender;
import cn.mmf.slashblade_tic.item.ItemLoader;
import mods.flammpfeil.slashblade.tileentity.DummyTileEntity;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.common.ModelRegisterUtil;

public class ModelReg {
	static final ModelResourceLocation modelLoc = new ModelResourceLocation("flammpfeil.slashblade:model/named/blade.obj");

	@SideOnly(Side.CLIENT)
    public ModelReg() {
    	ItemLoader.registerRender();

    	
    	
        
    }


    @SuppressWarnings("deprecation")
	public static void Slashblade_model(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, modelLoc);
        ForgeHooksClient.registerTESRItemStack(item, 0, DummyTileEntity.class);
  	}
    
}
