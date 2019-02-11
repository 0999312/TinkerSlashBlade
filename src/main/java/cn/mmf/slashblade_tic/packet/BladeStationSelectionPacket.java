/*
 * Copyright (c) 2018-2019 <C4>
 *
 * This Java class is distributed as a part of the Construct's Armory mod.
 * Construct's Armory is open source and distributed under the GNU Lesser General Public License v3.
 * View the source code and license file on github: https://github.com/TheIllusiveC4/ConstructsArmory
 *
 * Some classes and assets are taken and modified from the parent mod, Tinkers' Construct.
 * Tinkers' Construct is open source and distributed under the MIT License.
 * View the source code on github: https://github.com/SlimeKnights/TinkersConstruct/
 * View the MIT License here: https://tldrlegal.com/license/mit-license
 */

package cn.mmf.slashblade_tic.packet;

import cn.mmf.slashblade_tic.blade.SlashBladeCore;
import cn.mmf.slashblade_tic.client.gui.GuiBladeStation;
import cn.mmf.slashblade_tic.container.ContainerBladeStation;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.WorldServer;
import slimeknights.mantle.inventory.BaseContainer;
import slimeknights.mantle.network.AbstractPacketThreadsafe;
import slimeknights.tconstruct.common.TinkerNetwork;

public class BladeStationSelectionPacket extends AbstractPacketThreadsafe {

    public SlashBladeCore blade;
    public int activeSlots;

    public BladeStationSelectionPacket() {
    }

    public BladeStationSelectionPacket(final SlashBladeCore blade, final int activeSlots) {
        this.blade = blade;
        this.activeSlots = activeSlots;
    }

    public void handleClientSafe(final NetHandlerPlayClient netHandler) {
        final Container container = Minecraft.getMinecraft().player.openContainer;
        if (container instanceof ContainerBladeStation) {
            ((ContainerBladeStation)container).setToolSelection(this.blade, this.activeSlots);
            if (Minecraft.getMinecraft().currentScreen instanceof GuiBladeStation) {
                ((GuiBladeStation)Minecraft.getMinecraft().currentScreen).onToolSelectionPacket(this);
            }
        }
    }

    public void handleServerSafe(final NetHandlerPlayServer netHandler) {
        final Container container = netHandler.player.openContainer;
        if (container instanceof ContainerBladeStation) {
            ((ContainerBladeStation)container).setToolSelection(this.blade, this.activeSlots);
            final WorldServer server = netHandler.player.getServerWorld();
            for (final EntityPlayer player : server.playerEntities) {
                if (player == netHandler.player) {
                    continue;
                }
                if (!(player.openContainer instanceof ContainerBladeStation) || !((BaseContainer)container).sameGui((BaseContainer)player.openContainer)) {
                    continue;
                }
                ((ContainerBladeStation)player.openContainer).setToolSelection(this.blade, this.activeSlots);
                TinkerNetwork.sendTo(this, (EntityPlayerMP)player);
            }
        }
    }

    public void fromBytes(final ByteBuf buf) {
        final int id = buf.readShort();
        if (id > -1) {
            final Item item = Item.getItemById(id);
            if (item instanceof SlashBladeCore) {
                this.blade = (SlashBladeCore)item;
            }
        }
        this.activeSlots = buf.readInt();
    }

    public void toBytes(final ByteBuf buf) {
        if (this.blade == null) {
            buf.writeShort(-1);
        }
        else {
            buf.writeShort(Item.getIdFromItem(this.blade));
        }
        buf.writeInt(this.activeSlots);
    }
}
