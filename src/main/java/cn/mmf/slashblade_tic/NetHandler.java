package cn.mmf.slashblade_tic;

import cn.mmf.slashblade_tic.packet.BladeStationSelectionPacket;
import cn.mmf.slashblade_tic.packet.BladeStationTextPacket;
import slimeknights.tconstruct.common.TinkerNetwork;

public class NetHandler {
	public NetHandler() {
		TinkerNetwork.instance.registerPacket(BladeStationSelectionPacket.class);
		TinkerNetwork.instance.registerPacket(BladeStationTextPacket.class);
	}
}
