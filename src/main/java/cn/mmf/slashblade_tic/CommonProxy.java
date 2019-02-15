package cn.mmf.slashblade_tic;

import cn.mmf.slashblade_tic.blade.TinkerSlashBladeEvent;
import cn.mmf.slashblade_tic.item.RegisterLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.tools.ToolPart;
import slimeknights.tconstruct.tools.TinkerTools;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event)
	{
		new RegisterLoader(event);
		new NetHandler();
	}
	
    public void init(FMLInitializationEvent event)
    { 
//        MinecraftForge.EVENT_BUS.register(new TinkerSlashBladeEvent());
    	
    }

    public void postInit(FMLPostInitializationEvent event)
    {
    	

    }

}
