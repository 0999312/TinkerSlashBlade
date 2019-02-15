package cn.mmf.slashblade_tic.block.tileentity;

import cn.mmf.slashblade_tic.client.gui.GuiBladeForge;
import cn.mmf.slashblade_tic.container.ContainerBladeForge;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileBladeForge extends TileBladeStation {

    public TileBladeForge() {
        inventoryTitle = "gui.bladeforge.name";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer createGui(InventoryPlayer inventoryPlayer, World world, BlockPos pos) {
        return new GuiBladeForge(inventoryPlayer, world, pos, this);
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryPlayer, World world, BlockPos pos) {
        return new ContainerBladeForge(inventoryPlayer, this);
    }
}
