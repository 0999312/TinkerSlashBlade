package cn.mmf.slashblade_tic.compat.tinkertoolleveling;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.toolleveling.ToolLevelNBT;

import javax.annotation.Nonnull;

import cn.mmf.slashblade_tic.blade.SlashBladeCore;

public class CommandLevelSlashBlade extends CommandBase {

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Nonnull
    @Override
    public String getName() {
        return "levelupArmor";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/levelupArmor while holding a tinker armor in your hand";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {

        EntityPlayer player = getCommandSenderAsPlayer(sender);
        ItemStack itemStack = player.getHeldItemMainhand();

        if(!itemStack.isEmpty() && itemStack.getItem() instanceof SlashBladeCore) {
            int xp;
            if(args.length > 0) {
                xp = parseInt(args[0]);
            }
            else {
                ToolLevelNBT data = new ToolLevelNBT(TinkerUtil.getModifierTag(itemStack, ModBladeLeveling.modLeveling.getModifierIdentifier()));
                xp = ModBladeLeveling.modLeveling.getXpForLevelup(data.level);
            }
            ModBladeLeveling.modLeveling.addXp(itemStack, xp, player);
        }
        else {
            throw new CommandException("No tinker armor in hand");
        }
    }
}
