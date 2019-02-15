package cn.mmf.slashblade_tic.block.tileentity;

import cn.mmf.slashblade_tic.client.gui.GuiBladeStation;
import cn.mmf.slashblade_tic.container.ContainerBladeStation;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.tools.common.tileentity.TileToolStation;

public class TileBladeStation extends TileToolStation {

    public TileBladeStation() {
        inventoryTitle = "gui.bladestation.name";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer createGui(InventoryPlayer inventoryPlayer, World world, BlockPos pos) {
        return new GuiBladeStation(inventoryPlayer, world, pos, this);
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryPlayer, World world, BlockPos pos) {
        return new ContainerBladeStation(inventoryPlayer, this);
    }
}
