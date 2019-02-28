package cn.mmf.slashblade_tic.item;

import java.awt.*;
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
import cn.mmf.slashblade_tic.blade.ItemSlashBladeTICWhite;
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
	public static BladePart wrapper,handle,blade;
	public static SlashBladeCore sb,sb_white;
	public static Block bladestation;
	public static BlockBladeForge bladeforge;
	public static Item book_smith;
	public RegisterLoader(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}
	@SubscribeEvent
	public static void initItems(RegistryEvent.Register<Item> event) {
		book_smith = new ItemBookBladeSmith().setUnlocalizedName(Main.MODID+".book_smith");
		register(book_smith);
		
		 wrapper = (BladePart) new BladePart(Material.VALUE_Ingot * 8)
				 .setUnlocalizedName(Main.MODID+".slashblade.saya");
		 register(wrapper);
		 TinkerSlashBladeRegistry.registerToolPart(wrapper);
		 blade = (BladePart) new BladePart(Material.VALUE_Ingot * 4)
				 .setUnlocalizedName(Main.MODID+".slashblade.blade");
		 register(blade);
		 TinkerSlashBladeRegistry.registerToolPart(wrapper);
		 handle = (BladePart) new BladePart(Material.VALUE_Ingot * 2)
				 .setUnlocalizedName(Main.MODID+".slashblade.handle");
		 register(handle);
		 TinkerSlashBladeRegistry.registerToolPart(wrapper);
		 TinkerSlashBladeRegistry.registerToolPart(blade);
		 TinkerSlashBladeRegistry.registerToolPart(handle);
		 
		sb_white = (ItemSlashBladeTICWhite) new ItemSlashBladeTICWhite().setUnlocalizedName(Main.MODID+".slashblade.white");
		 register(sb_white);
		 TinkerSlashBladeRegistry.registerTool(sb_white);
		 TinkerSlashBladeRegistry.registerToolCrafting(sb_white);
		 
	     sb = (ItemSlashBladeTIC) new ItemSlashBladeTIC().setUnlocalizedName(Main.MODID+".slashblade");
	     register(sb);
	    TinkerSlashBladeRegistry.registerTool(sb);
		TinkerSlashBladeRegistry.registerToolForgeCrafting(sb);	

		for(SlashBladeCore sb : TinkerSlashBladeRegistry.getTools()){
			for (final PartMaterialType pmt: sb.getRequiredComponents()) {
				if (pmt.getPossibleParts().contains(wrapper)) 
					TinkerRegistry.registerStencilTableCrafting(Pattern.setTagForPart(new ItemStack(TinkerTools.pattern), wrapper));
				if (pmt.getPossibleParts().contains(blade)) 
					TinkerRegistry.registerStencilTableCrafting(Pattern.setTagForPart(new ItemStack(TinkerTools.pattern), blade));
				if (pmt.getPossibleParts().contains(handle)) 
					TinkerRegistry.registerStencilTableCrafting(Pattern.setTagForPart(new ItemStack(TinkerTools.pattern), handle));
			}
		}
	}
	@SideOnly(Side.CLIENT)
	public static void RegisterColor(SlashBladeCore sb,String name) throws IOException {
		TextureMixer texture_mixer = TextureMixer.getInstance();
		List<ResourceLocationRaw> list_model = sb.getMuitlModelTexture();
		IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
		   BufferedImage[] img = new BufferedImage[3],img2 = new BufferedImage[3], altered = new BufferedImage[3], alteredbg = new BufferedImage[3];

		   InputStream imageStream,imageStream2;
		for(int i = 0; i<list_model.size()-3;i++){
			imageStream = manager.getResource(list_model.get(i)).getInputStream();
			imageStream2 = manager.getResource(list_model.get(i+3)).getInputStream();
			try
			{
				alteredbg[i]=ImageIO.read(imageStream);
				img2[i]=ImageIO.read(imageStream2);
				
				altered[i] = new BufferedImage(img2[i].getWidth(), img2[i].getHeight(), BufferedImage.TYPE_INT_ARGB);

			}
			finally
			{
				IOUtils.closeQuietly(imageStream);
				IOUtils.closeQuietly(imageStream2);
			}
		}

		for(Material mat : TinkerRegistry.getAllMaterials()){
			BufferedImage[] dest = new BufferedImage[3];
			for (int i = 0; i < list_model.size()-3 ; i++){
				dest[i] = TextureMixer.getOverlay(mat.materialTextColor, alteredbg[i], altered[i]);
			}
			texture_mixer.MapColor(mat.identifier+"_"+name, dest);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void RegisterModel(ModelRegistryEvent event) {
		ModelReg.registerRender(book_smith);
		
		ModelRegisterUtil.registerPartModel(RegisterLoader.wrapper);
		ModelRegisterUtil.registerPartModel(RegisterLoader.handle);
		ModelRegisterUtil.registerPartModel(RegisterLoader.blade);
		try {
			RegisterColor(sb,"slashblade");
			RegisterColor(sb_white,"slashblade_white");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ModelReg.Slashblade_model(sb);
		ModelReg.Slashblade_model(sb_white);
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

	@SideOnly(Side.CLIENT)
    @SubscribeEvent (priority = EventPriority.LOW)
    public static void modelBake(ModelBakeEvent evt) {
        ToolClientEvents.replaceTableModel(new ModelResourceLocation(bladeforge.getRegistryName(), "normal"), evt);
        ToolClientEvents.replaceTableModel(new ModelResourceLocation(bladestation.getRegistryName(), "normal"), evt);
        ToolClientEvents.replaceTableModel(new ModelResourceLocation(bladeforge.getRegistryName(), "inventory"), evt);
        ToolClientEvents.replaceTableModel(new ModelResourceLocation(bladestation.getRegistryName(), "inventory"), evt);
    }
}
