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

package cn.mmf.slashblade_tic.container;

import java.util.Set;

import cn.mmf.slashblade_tic.blade.SlashBladeCore;
import cn.mmf.slashblade_tic.blade.TinkerSlashBladeRegistry;
import cn.mmf.slashblade_tic.block.tileentity.TileBladeForge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.tools.ToolCore;

public class ContainerBladeForge extends ContainerBladeStation {

    public ContainerBladeForge(InventoryPlayer playerInventory, TileBladeForge tile) {
        super(playerInventory, tile);

    }
    @Override
    protected Set<SlashBladeCore> getBuildableTools() {
      return TinkerSlashBladeRegistry.getToolForgeCrafting();
    }
    @Override
    protected void playCraftSound(EntityPlayer player) {
        Sounds.playSoundForAll(player, SoundEvents.BLOCK_ANVIL_USE, 0.9f, 0.95f + 0.2f * TConstruct.random.nextFloat());
    }
}
