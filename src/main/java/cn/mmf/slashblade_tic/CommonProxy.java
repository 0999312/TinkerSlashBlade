package cn.mmf.slashblade_tic;

import cn.mmf.slashblade_tic.blade.TinkerSlashBladeEvent;
import cn.mmf.slashblade_tic.blade.TinkerSlashBladeRegistry;
import cn.mmf.slashblade_tic.compat.tinkertoolleveling.ModBladeLeveling;
import cn.mmf.slashblade_tic.item.RegisterLoader;
import cn.mmf.slashblade_tic.modifiers.ModProud;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.mantle.util.RecipeMatchRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.ToolPart;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.modifiers.ModAntiMonsterType;
import slimeknights.tconstruct.tools.modifiers.ModBeheading;
import slimeknights.tconstruct.tools.modifiers.ModCreative;
import slimeknights.tconstruct.tools.modifiers.ModDiamond;
import slimeknights.tconstruct.tools.modifiers.ModEmerald;
import slimeknights.tconstruct.tools.modifiers.ModFiery;
import slimeknights.tconstruct.tools.modifiers.ModKnockback;
import slimeknights.tconstruct.tools.modifiers.ModNecrotic;
import slimeknights.tconstruct.tools.modifiers.ModReinforced;
import slimeknights.tconstruct.tools.modifiers.ModSharpness;
import slimeknights.tconstruct.tools.modifiers.ModSilktouch;
import slimeknights.tconstruct.tools.modifiers.ModSoulbound;
import slimeknights.tconstruct.tools.modifiers.ModWebbed;

public class CommonProxy {
	 public static Modifier modProud;

	public void preInit(FMLPreInitializationEvent event)
	{
		new RegisterLoader(event);
		new NetHandler();
	}
	
    public void init(FMLInitializationEvent event)
    { 

    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modBaneOfArthopods);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modBeheading);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modCreative);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modDiamond);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modEmerald);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modFiery);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modKnockback);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modNecrotic);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modSmite);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modReinforced);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modSharpness);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modSoulbound);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modWebbed);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modShulking);
    	TinkerSlashBladeRegistry.registerModifier(TinkerModifiers.modMendingMoss);
    	modProud=new ModProud();
    	modProud.addItem(SlashBlade.findItemStack(SlashBlade.modid, SlashBlade.TrapezohedronBladeSoulStr, 1), 1, 1);
    	TinkerSlashBladeRegistry.registerModifier(modProud);
    }


	public void postInit(FMLPostInitializationEvent event)
    {
        if (Loader.isModLoaded("tinkertoolleveling")) {
            ModBladeLeveling.modLeveling = new ModBladeLeveling();
        }

    }

}
