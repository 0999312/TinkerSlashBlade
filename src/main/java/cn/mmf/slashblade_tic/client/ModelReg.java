package cn.mmf.slashblade_tic.client;

import cn.mmf.slashblade_tic.item.RegisterLoader;
import mods.flammpfeil.slashblade.tileentity.DummyTileEntity;
import net.minecraft.block.Block;
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
    @SuppressWarnings("deprecation")
	public static void Slashblade_model(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, modelLoc);
        ForgeHooksClient.registerTESRItemStack(item, 0, DummyTileEntity.class);
  	}
    @SideOnly(Side.CLIENT)
    public static void registerRender(Block block)
    {
        ModelResourceLocation model = new ModelResourceLocation(block.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, model);
    }
}
