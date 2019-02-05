package cn.mmf.slashblade_tic.client;

import org.lwjgl.input.Keyboard;

import cn.mmf.slashblade_tic.CommonProxy;
import cn.mmf.slashblade_tic.client.model.BladeModelManager;
import cn.mmf.slashblade_tic.client.model.BladeSpecialRender;
import cn.mmf.slashblade_tic.client.model.NullTE;
import cn.mmf.slashblade_tic.item.ItemLoader;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class ClientProxy extends CommonProxy {
	public static final ModelResourceLocation modelLoc = new ModelResourceLocation("flammpfeil.slashblade:model/named/blade.obj","inventory");

		@Override
	    public void preInit(FMLPreInitializationEvent event)
	    {
	        super.preInit(event);
	        MinecraftForge.EVENT_BUS.register(BladeModelManager.getInstance());
	      
	        ClientRegistry.bindTileEntitySpecialRenderer(NullTE.class, new BladeSpecialRender());
	    }


	    @Override
	    public void init(FMLInitializationEvent event)
	    {
	        super.init(event);

	    }

	    @Override
	    public void postInit(FMLPostInitializationEvent event)
	    {
	        super.postInit(event);
	       
	    }

}
