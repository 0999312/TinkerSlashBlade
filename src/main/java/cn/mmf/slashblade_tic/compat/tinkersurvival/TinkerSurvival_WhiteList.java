package cn.mmf.slashblade_tic.compat.tinkersurvival;

import java.lang.reflect.Field;
import java.util.Set;

import cn.mmf.slashblade_tic.ConfigHandler;
import cn.mmf.slashblade_tic.Main;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.crash.CrashReport;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class TinkerSurvival_WhiteList {
	Class itemUse;
	public TinkerSurvival_WhiteList() {
		try {
			itemUse = Class.forName("tinkersurvival.util.ItemUse");
			Field toolModidsField = itemUse.getDeclaredField("toolModids");
			toolModidsField.setAccessible(true);
			Set<String> toolModids = (Set<String>) toolModidsField.get(null);
			if(!toolModids.contains(Main.MODID))
			toolModids.add(Main.MODID);

			for(Item item : ForgeRegistries.ITEMS){
				if(item instanceof ItemSlashBlade&&!toolModids.contains(item.getRegistryName().getResourceDomain()))
					toolModids.add(item.getRegistryName().getResourceDomain());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
