package cn.mmf.slashblade_tic.item;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

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
import cn.mmf.slashblade_tic.util.TextureMixer;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.ResourceLocationRaw;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tinkering.TinkersItem;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.Pattern;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.tools.ToolPart;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.ToolClientEvents;
import slimeknights.tconstruct.tools.common.TableRecipeFactory;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class RegisterLoader {
	public static BladePart wrapper;
	public static ItemSlashBladeTIC sb;
	public static Block bladestation;
	public static BlockBladeForge bladeforge;
	public RegisterLoader(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}
	@SubscribeEvent
	public static void initItems(RegistryEvent.Register<Item> event) {
		 wrapper = (BladePart) new BladePart(Material.VALUE_Ingot * 8)
				 .setUnlocalizedName(Main.MODID+".slashblade.saya");
		 register(wrapper);
		 TinkerSlashBladeRegistry.registerToolPart(wrapper);

		 
	     sb = (ItemSlashBladeTIC) new ItemSlashBladeTIC().setUnlocalizedName(Main.MODID+".slashblade");
	     register(sb);
	    TinkerSlashBladeRegistry.registerTool(sb);
		TinkerSlashBladeRegistry.registerToolCrafting(sb);	
		 
		for (final PartMaterialType pmt: sb.getRequiredComponents()) {
			if (pmt.getPossibleParts().contains(wrapper)) {
				TinkerRegistry.registerStencilTableCrafting(Pattern.setTagForPart(new ItemStack(TinkerTools.pattern), wrapper));
			}
		}

	}
	@SideOnly(Side.CLIENT)
	public static void RegisterColor() throws IOException {
		TextureMixer texture_mixer = new TextureMixer();
		List<ResourceLocationRaw> list_model = sb.getMuitlModelTexture();
		IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
		   BufferedImage img = null,img2 = null;
		   InputStream imageStream,imageStream2;
	        for(int i = 0; i<list_model.size()-3;i++){
	        	imageStream = manager.getResource(list_model.get(i)).getInputStream();
	        	imageStream2 = manager.getResource(list_model.get(i+3)).getInputStream();
	        	try
	            {
	        		img=ImageIO.read(imageStream);
	        		img2=ImageIO.read(imageStream2);
	        		for(Material mat : TinkerRegistry.getAllMaterials()){
	        			texture_mixer.addColor(mat.renderInfo.getVertexColor(), img, img2);
	        		}
	            }
	            finally
	            {
	                IOUtils.closeQuietly(imageStream);
	                IOUtils.closeQuietly(imageStream2);
	        	}
	        }

	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void RegisterModel(ModelRegistryEvent event) {
		ModelRegisterUtil.registerPartModel(RegisterLoader.wrapper);
		try {
			RegisterColor();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ModelReg.Slashblade_model(sb);
		ModelReg.registerRender(bladestation);
		ModelReg.registerRender(bladeforge);
	}
	@SubscribeEvent
	public static void RegisterBlock(RegistryEvent.Register<Block> event) {
		bladestation = new BlockBladeStation().setUnlocalizedName(Main.MODID+"."+"blade_station");
		bladeforge = (BlockBladeForge) new BlockBladeForge().setUnlocalizedName(Main.MODID+"."+"blade_forge");
		register(bladestation);
		register(bladeforge);
		GameRegistry.registerTileEntity(TileBladeStation.class, new ResourceLocation(Main.MODID, "blade_station"));
		GameRegistry.registerTileEntity(TileBladeForge.class, new ResourceLocation(Main.MODID, "blade_forge"));
	}
	

   
	@SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        IForgeRegistry<IRecipe> registry = event.getRegistry();

        registry.register(new ShapelessOreRecipe(new ResourceLocation(Main.MODID,"blade_station"), bladestation, new Object[]{
        		SlashBlade.bladeWood,TinkerTools.toolTables
        }).setRegistryName(new ResourceLocation(Main.MODID,"blade_station")));
        if (bladeforge != null) {
        	bladeforge.baseBlocks.addAll(TinkerTools.toolForge.baseBlocks);
            for (String oredict : bladeforge.baseBlocks) {
                Block brick = TinkerSmeltery.searedBlock;
                
                if(brick == null) {
                    brick = Blocks.STONEBRICK;
                }

                TableRecipeFactory.TableRecipe recipe =
                        new TableRecipeFactory.TableRecipe(
                                new ResourceLocation(Main.MODID, "blade_forge"),
                                new OreIngredient(oredict),
                                new ItemStack(bladeforge),
                                CraftingHelper.parseShaped("BBB", "MTM", "M M",
                                        'B', brick,
                                        'M', oredict,
                                        'T', bladestation));
                recipe.setRegistryName("blade_forge_" + oredict.toLowerCase(Locale.US));
                registry.register(recipe);
            }
        }
        
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
//    private static void registerStencil(IToolPart armorPart) {
//    	System.out.println("|WO)");
//        for(SlashBladeCore armorCore : TinkerSlashBladeRegistry.getTools()) {
//        	System.out.println("|MO)");
//            for(PartMaterialType partMaterialType : armorCore.getRequiredComponents()) {
//            	System.out.println("(OMO) |WO)");
//                if(partMaterialType.getPossibleParts().contains(armorPart)) {
//                	System.out.println("(OMO):为什么只是看着");
//                    
//                	return;
//                }
//            }
//        }
//        
//    }
    @SubscribeEvent (priority = EventPriority.LOW)
    public static void modelBake(ModelBakeEvent evt) {
        ToolClientEvents.replaceTableModel(new ModelResourceLocation(bladeforge.getRegistryName(), "inventory"), evt);
        ToolClientEvents.replaceTableModel(new ModelResourceLocation(bladestation.getRegistryName(), "inventory"), evt);
    }
}
