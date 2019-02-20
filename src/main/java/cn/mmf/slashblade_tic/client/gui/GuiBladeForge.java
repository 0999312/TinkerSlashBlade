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

package cn.mmf.slashblade_tic.client.gui;

import java.util.Set;

import cn.mmf.slashblade_tic.blade.SlashBladeCore;
import cn.mmf.slashblade_tic.blade.TinkerSlashBladeRegistry;
import cn.mmf.slashblade_tic.block.tileentity.TileBladeStation;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiBladeForge extends GuiBladeStation {

    public GuiBladeForge(InventoryPlayer player, World world, BlockPos pos, TileBladeStation tile) {
        super(player, world, pos, tile);
        metal();
    }
    
    public Set<SlashBladeCore> getBuildableItems() {
        return TinkerSlashBladeRegistry.getToolForgeCrafting();
      }

}
