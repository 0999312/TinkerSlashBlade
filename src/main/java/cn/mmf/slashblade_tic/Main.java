package cn.mmf.slashblade_tic;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION,dependencies="required-after:forge@[14.23.4.2768,);required-after:flammpfeil.slashblade@[mc1.12-r17,);required-after:tconstruct;after:lastsmith;")
public class Main
{
    public static final String MODID = "slashbladetic";
    public static final String NAME = "Tinker SlashBlade";
    public static final String VERSION = "@version@";

	@Instance(Main.MODID)
    public static Main instance;
    
	@SidedProxy(clientSide = "cn.mmf.slashblade_tic.client.ClientProxy",serverSide = "cn.mmf.slashblade_tic.CommonProxy")
	public static CommonProxy proxy; 
	 
	   
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
	    proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
	    proxy.init(event);
	
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	    proxy.postInit(event);
	}
}   