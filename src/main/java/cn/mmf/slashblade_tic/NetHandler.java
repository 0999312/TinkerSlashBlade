package cn.mmf.slashblade_tic;

import cn.mmf.slashblade_tic.compat.tinkertoolleveling.ConfigSyncLevelingPacket;
import cn.mmf.slashblade_tic.packet.BladeStationSelectionPacket;
import cn.mmf.slashblade_tic.packet.BladeStationTextPacket;
import net.minecraftforge.fml.common.Loader;
import slimeknights.tconstruct.common.TinkerNetwork;

public class NetHandler {
	public NetHandler() {
		TinkerNetwork.instance.registerPacket(BladeStationSelectionPacket.class);
		TinkerNetwork.instance.registerPacket(BladeStationTextPacket.class);
		TinkerNetwork.instance.registerPacket(ConfigSyncLevelingPacket.class);
	}
}
