package cn.mmf.slashblade_tic.item;

import cn.mmf.slashblade_tic.Main;
import cn.mmf.slashblade_tic.blade.ItemSlashBladeTIC;
import cn.mmf.slashblade_tic.blade.SlashBladeCore;
import cn.mmf.slashblade_tic.blade.SlashBladeTICBasic;
import cn.mmf.slashblade_tic.blade.TinkerSlashBladeRegistry;
import cn.mmf.slashblade_tic.block.BlockBladeForge;
import cn.mmf.slashblade_tic.block.BlockBladeStation;
import cn.mmf.slashblade_tic.block.tileentity.TileBladeForge;
import cn.mmf.slashblade_tic.block.tileentity.TileBladeStation;
import cn.mmf.slashblade_tic.client.ModelReg;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tinkering.TinkersItem;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.Pattern;
import slimeknights.tconstruct.library.tools.ToolPart;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTools;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class RegisterLoader {
	public static ItemSlashBladeSaya wrapper;
	public static ItemSlashBladeTIC sb;
	public static Block bladestation,bladeforge;
	public RegisterLoader(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}
	@SubscribeEvent(priority = EventPriority.LOW)
	public static void initItems(RegistryEvent.Register<Item> event) {
	  	 wrapper = (ItemSlashBladeSaya)(new ItemSlashBladeSaya(ToolMaterial.IRON))
	             .setMaxDamage(0)
	             .setUnlocalizedName("flammpfeil.slashblade.saya.test")
	             .setCreativeTab(TinkerRegistry.tabParts);
	     register(wrapper);
	     TinkerSlashBladeRegistry.registerToolPart(wrapper);
	     registerStencil(TinkerTools.pattern, wrapper);
	     new ModelReg();
	    
	     sb = (ItemSlashBladeTIC) new ItemSlashBladeTIC().setUnlocalizedName("flammpfeil.slashblade.test");
	     register(sb);
	    TinkerSlashBladeRegistry.registerTool(sb);
		TinkerSlashBladeRegistry.registerToolCrafting(sb);		
		registerRender();
	}
	@SubscribeEvent(priority = EventPriority.LOW)
	public static void RegisterBlock(RegistryEvent.Register<Block> event) {
		bladestation = new BlockBladeStation().setUnlocalizedName(Main.MODID+"."+"blade_station");
		bladeforge = new BlockBladeForge().setUnlocalizedName(Main.MODID+"."+"blade_forge");
		register(bladestation);
		register(bladeforge);
		 GameRegistry.registerTileEntity(TileBladeStation.class, new ResourceLocation(Main.MODID, "blade_station"));
		 GameRegistry.registerTileEntity(TileBladeForge.class, new ResourceLocation(Main.MODID, "blade_forge"));
	}
	@SideOnly(Side.CLIENT)
	public static void registerRender() {
		ModelReg.Slashblade_model(wrapper);
		ModelReg.Slashblade_model(sb);
	}
   
    
	private static void register(Item item)
    {
        ForgeRegistries.ITEMS.register(item.setRegistryName(item.getUnlocalizedName().substring(5+Main.MODID.length()+1)));
        
    }
	private static void register(Block item)
    {
        ForgeRegistries.BLOCKS.register(item.setRegistryName(item.getUnlocalizedName().substring(5+Main.MODID.length()+1)));
        Item blockitem = new ItemBlock(item).setUnlocalizedName(item.getUnlocalizedName().substring(5));
        register(blockitem);
    }
    private static void registerStencil(Item pattern, IToolPart armorPart) {
        for(SlashBladeCore armorCore : TinkerSlashBladeRegistry.getTools()) {
            for(PartMaterialType partMaterialType : armorCore.getRequiredComponents()) {
                if(partMaterialType.getPossibleParts().contains(armorPart)) {
                    ItemStack stencil = new ItemStack(pattern);
                    Pattern.setTagForPart(stencil, (Item) armorPart);
                    TinkerRegistry.registerStencilTableCrafting(stencil);
                    return;
                }
            }
        }
    }
}
