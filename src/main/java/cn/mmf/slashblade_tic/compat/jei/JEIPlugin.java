package cn.mmf.slashblade_tic.compat.jei;

import javax.annotation.Nonnull;

import cn.mmf.slashblade_tic.blade.SlashBladeCore;
import cn.mmf.slashblade_tic.blade.TinkerSlashBladeRegistry;
import cn.mmf.slashblade_tic.item.RegisterLoader;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.ISubtypeRegistry;
import net.minecraft.item.Item;
import slimeknights.tconstruct.plugin.jei.interpreter.TableSubtypeInterpreter;
import slimeknights.tconstruct.plugin.jei.interpreter.ToolSubtypeInterpreter;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {

    @Override
    public void registerItemSubtypes(@Nonnull ISubtypeRegistry registry) {

        ToolSubtypeInterpreter armorInterpreter = new ToolSubtypeInterpreter();
        TableSubtypeInterpreter tableInterpreter = new TableSubtypeInterpreter();

        for (SlashBladeCore armor : TinkerSlashBladeRegistry.getTools()) {
            registry.registerSubtypeInterpreter(armor, armorInterpreter);
        }

        registry.registerSubtypeInterpreter(Item.getItemFromBlock(RegisterLoader.bladeforge), tableInterpreter);
    }
}