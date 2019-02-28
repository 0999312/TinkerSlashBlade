package cn.mmf.slashblade_tic.compat.tinkertoolleveling;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import slimeknights.mantle.network.AbstractPacketThreadsafe;

public class ConfigSyncLevelingPacket extends AbstractPacketThreadsafe {

    public int newMinModifiers;
    public int maximumLevels;
    public int baseXP;
    public float levelMultiplier;

    public ConfigSyncLevelingPacket() {}

    public ConfigSyncLevelingPacket(int minModifiers, int maxLevels, int xp, float levelMult) {
        newMinModifiers = minModifiers;
        maximumLevels = maxLevels;
        baseXP = xp;
        levelMultiplier = levelMult;
    }

    public void handleClientSafe(final NetHandlerPlayClient netHandler) {
    	ModBladeLeveling.newMinModifiers = newMinModifiers;
        ModBladeLeveling.maximumLevels = maximumLevels;
        ModBladeLeveling.baseXP = baseXP;
        ModBladeLeveling.levelMultiplier = levelMultiplier;
    }

    public void handleServerSafe(final NetHandlerPlayServer netHandler) {
        throw new UnsupportedOperationException("Client side only!");
    }

    public void fromBytes(final ByteBuf buf) {
        this.newMinModifiers = buf.readInt();
        this.maximumLevels = buf.readInt();
        this.baseXP = buf.readInt();
        this.levelMultiplier = buf.readFloat();
    }

    public void toBytes(final ByteBuf buf) {
        buf.writeInt(newMinModifiers);
        buf.writeInt(maximumLevels);
        buf.writeInt(baseXP);
        buf.writeFloat(levelMultiplier);
    }
}
