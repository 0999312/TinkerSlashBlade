package cn.mmf.slashblade_tic.item;

import cn.mmf.slashblade_tic.Main;
import cn.mmf.slashblade_tic.ModelReg;
import cn.mmf.slashblade_tic.blade.ItemSlashBladeTIC;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.Pattern;
import slimeknights.tconstruct.library.tools.ToolPart;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTools;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class ItemLoader {
	public static ItemSlashBladeSaya wrapper;
	public static ItemSlashBladeTIC sb;
	public ItemLoader(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}
	@SubscribeEvent(priority = EventPriority.LOW)
	public static void initItems(RegistryEvent.Register<Item> event) {
	  	 wrapper = (ItemSlashBladeSaya)(new ItemSlashBladeSaya(ToolMaterial.IRON))
	             .setMaxDamage(0)
	             .setUnlocalizedName("flammpfeil.slashblade.saya.test")
	             .setCreativeTab(TinkerRegistry.tabParts);
	     register(wrapper);
	     TinkerRegistry.registerToolPart(wrapper);
	     TinkerRegistry.registerStencilTableCrafting(Pattern.setTagForPart(new ItemStack(TinkerTools.pattern),wrapper));
	     
	     new ModelReg();
	    
	     sb = (ItemSlashBladeTIC) new ItemSlashBladeTIC().setUnlocalizedName("flammpfeil.slashblade.test");
	     register(sb);
		TinkerSlashBladeRegistry.registerToolCrafting(sb);			
	}
	@SideOnly(Side.CLIENT)
	public static void registerRender() {
		ModelReg.Slashblade_model(wrapper);
	}
   
    
	private static void register(Item item)
    {
        ForgeRegistries.ITEMS.register(item.setRegistryName(item.getUnlocalizedName().substring(5+Main.MODID.length()+1)));
        
    }

	
}
