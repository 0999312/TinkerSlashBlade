package cn.mmf.slashblade_tic.blade;

import java.util.ArrayList;
import java.util.List;

import cn.mmf.slashblade_tic.util.SlashBladeHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.utils.TinkerUtil;

public class SlashBladeTraitEvents {
	  /**
	   * Handles the onBlock or the onPlayerHurt trait callback. Note that only one of the two is called!
	   */
	  @SubscribeEvent
	  public void playerBlockOrHurtEvent(LivingHurtEvent event) {
	    boolean isPlayerGettingDamaged = event.getEntityLiving() instanceof EntityPlayer;
	    boolean isClient = event.getEntityLiving().getEntityWorld().isRemote;
	    boolean isReflectedDamage = event.getSource() instanceof EntityDamageSource && ((EntityDamageSource) event.getSource()).getIsThornsDamage();

	    if(!isPlayerGettingDamaged || isClient || isReflectedDamage) {
	      return;
	    }
	    final EntityPlayer player = (EntityPlayer) event.getEntityLiving();
	    Entity attacker = event.getSource().getTrueSource();

	    List<ItemStack> heldTools = new ArrayList<>();
	    for(ItemStack tool : event.getEntity().getHeldEquipment()) {
	      if(isTool(tool) && !SlashBladeHelper.isBroken(tool)) {
	        heldTools.add(tool);
	      }
	    }

	    // first handle block
	    if(player.isActiveItemStackBlocking()) {
	      // we allow block traits to affect both main and offhand
	      for(ItemStack tool : heldTools) {
	        if(!event.isCanceled()) {
	          TinkerUtil.getTraitsOrdered(tool).forEach(trait -> trait.onBlock(tool, player, event));
	        }
	      }
	    }
	    // else handle living hurt
	    else if(attacker instanceof EntityLivingBase && !attacker.isDead) {
	      // we allow block traits to affect both main and offhand
	      for(ItemStack tool : heldTools) {
	        if(!event.isCanceled()) {
	          TinkerUtil.getTraitsOrdered(tool).forEach(trait -> trait.onPlayerHurt(tool, player, (EntityLivingBase) attacker, event));
	        }
	      }
	    }
	  }

	  @SubscribeEvent
	  public void onRepair(TinkerSlashBladeEvent.OnRepair event) {
	    ItemStack tool = event.itemStack;

	    TinkerUtil.getTraitsOrdered(tool).forEach(trait -> trait.onRepair(tool, event.amount));
	  }

	  private boolean isTool(ItemStack stack) {
	    return stack != null && stack.getItem() instanceof SlashBladeCore;
	  }
}
