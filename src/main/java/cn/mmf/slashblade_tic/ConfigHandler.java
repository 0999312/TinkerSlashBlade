package cn.mmf.slashblade_tic;

import cn.mmf.slashblade_tic.compat.tinkertoolleveling.ConfigSyncLevelingPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import slimeknights.tconstruct.common.TinkerNetwork;
@Config(modid= Main.MODID)
public class ConfigHandler {
    public static final Leveling leveling = new Leveling();
	   public static class Leveling {
	        @Config.Name("Starting Modifier Amount")
	        @Config.Comment("Reduces the amount of modifiers a newly built blade gets if the value is lower than the regular amount of modifiers the blade would have.")
	        @Config.RequiresWorldRestart
	        public int newMinModifiers = 3;
	        @Config.Name("Maximum Levels")
	        @Config.Comment("Maximum achievable levels. If set to 0 or lower there is no upper limit.")
	        @Config.RequiresWorldRestart
	        public int maximumLevels = -1;
	        @Config.Name("Base XP Requirement")
	        @Config.Comment("Base XP needed for blade")
	        @Config.RequiresWorldRestart
	        public int baseXP = 100;
	        @Config.Name("Leveling Multiplier")
	        @Config.Comment("How much to multiply the experience needed for each level")
	        @Config.RequiresWorldRestart
	        public float levelMultiplier = 2.0F;
	    }
	    @Mod.EventBusSubscriber(modid = Main.MODID)
	    private static class ConfigEventHandler {
	        @SubscribeEvent
	        public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent evt) {
	            ConfigSyncLevelingPacket sync = new ConfigSyncLevelingPacket(leveling.newMinModifiers, leveling.maximumLevels, leveling.baseXP, leveling.levelMultiplier);
	            TinkerNetwork.sendTo(sync, (EntityPlayerMP) evt.player);
	        }
	    }
}
