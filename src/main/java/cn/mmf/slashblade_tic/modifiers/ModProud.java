package cn.mmf.slashblade_tic.modifiers;


import java.lang.reflect.Method;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;

public class ModProud extends ModifierTrait {

	public ModProud() {
		super("proud_soul", 0x8A2BE2);
		addAspects(new ModifierAspect.SingleAspect(this), new ModifierAspect.DataAspect(this), ModifierAspect.freeModifier);
	}
	@Override
	public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt,
			boolean wasCritical, boolean wasHit) {
	    // did the target die?
	    if(!target.isEntityAlive() && wasHit) {
	     incrementProudSoul(tool, target, player);
	    }
	}
	
    private static void incrementProudSoul(ItemStack stack, EntityLivingBase target,EntityLivingBase player){
        if(player instanceof EntityPlayer) {
            Method getExperiencePoints = ReflectionHelper.findMethod(EntityLivingBase.class, "getExperiencePoints", "func_70693_a", EntityPlayer.class);
            try {
                int exp = (Integer)getExperiencePoints.invoke(target, (EntityPlayer) player);
                exp = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(target, (EntityPlayer) player, exp);
                float rank = StylishRankManager.getStylishRank(player);
                exp *= 1.0 + rank *0.25;
                NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
                ItemSlashBlade.PrevExp.set(tag,exp);
                ItemSlashBlade.ProudSoul.add(tag,exp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
