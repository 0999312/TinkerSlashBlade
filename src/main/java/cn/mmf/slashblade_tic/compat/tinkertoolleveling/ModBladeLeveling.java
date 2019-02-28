package cn.mmf.slashblade_tic.compat.tinkertoolleveling;

import com.google.common.collect.Lists;

import cn.mmf.slashblade_tic.ConfigHandler;
import cn.mmf.slashblade_tic.blade.SlashBladeCore;
import cn.mmf.slashblade_tic.blade.TinkerSlashBladeEvent;
import cn.mmf.slashblade_tic.util.SlashBladeBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.client.CustomFontColor;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.library.modifiers.TinkerGuiException;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.toolleveling.TinkerToolLeveling;
import slimeknights.toolleveling.ToolLevelNBT;
import slimeknights.toolleveling.capability.CapabilityDamageXp;

import java.awt.*;
import java.util.List;

public class ModBladeLeveling extends ModifierTrait {

    public static ModBladeLeveling modLeveling;

    public static int newMinModifiers;
    public static int maximumLevels;
    public static int baseXP;
    public static float levelMultiplier;

    public ModBladeLeveling() {
        super("leveling", 0xffffff);
        aspects.clear();
        addAspects(new ModifierAspect.DataAspect(this));
        MinecraftForge.EVENT_BUS.register(this);

        newMinModifiers = ConfigHandler.leveling.newMinModifiers;
        maximumLevels = ConfigHandler.leveling.maximumLevels;
        baseXP = ConfigHandler.leveling.baseXP;
        levelMultiplier = ConfigHandler.leveling.levelMultiplier;
        
    }

    @SubscribeEvent
    public void onBuild(TinkerSlashBladeEvent.OnItemBuilding evt) {

        List<Material> materials = Lists.newArrayList();
        for(int i = 0; i < evt.tool.getRequiredComponents().size(); i++) {
            materials.add(Material.UNKNOWN);
        }
        NBTTagCompound baseTag = evt.tool.buildTag(materials);

        int modifiers = baseTag.getInteger(Tags.FREE_MODIFIERS);
        int modifierDelta = getStartingModifiers() - modifiers;

        NBTTagCompound toolTag = TagUtil.getToolTag(evt.tag);
        modifiers = toolTag.getInteger(Tags.FREE_MODIFIERS);
        modifiers += modifierDelta;
        modifiers = Math.max(0, modifiers);
        toolTag.setInteger(Tags.FREE_MODIFIERS, modifiers);
        TagUtil.setToolTag(evt.tag, toolTag);

        if(TinkerUtil.getModifierTag(evt.tag, getModifierIdentifier()).hasNoTags()) {
            apply(evt.tag);
        }

        if(!TinkerUtil.hasModifier(evt.tag, getModifierIdentifier())) {
            apply(evt.tag);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent evt) {
        Tooltips.addTooltips(evt.getItemStack(), evt.getToolTip());
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
      if(!target.getEntityWorld().isRemote && wasHit && player instanceof EntityPlayer) {
        // if we killed it the event for distributing xp was already fired and we just do it manually here
        EntityPlayer entityPlayer = (EntityPlayer) player;
        if(!target.isEntityAlive()) {
          addXp(tool, Math.round(damageDealt), entityPlayer);
        }
        else if(target.hasCapability(CapabilityDamageXp.CAPABILITY, null)) {
          target.getCapability(CapabilityDamageXp.CAPABILITY, null).addDamageFromTool(damageDealt, tool, entityPlayer);
        }
        
      }
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public boolean canApplyCustom(ItemStack stack) {
        return true;
    }

    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        super.applyEffect(rootCompound, modifierTag);

        ToolLevelNBT data = getLevelData(modifierTag);

        NBTTagCompound toolTag = TagUtil.getToolTag(rootCompound);
        int modifiers = toolTag.getInteger(Tags.FREE_MODIFIERS) + data.bonusModifiers;
        toolTag.setInteger(Tags.FREE_MODIFIERS, Math.max(0, modifiers));
        TagUtil.setToolTag(rootCompound, toolTag);
    }

    public void addXp(ItemStack blade, int amount, EntityPlayer player) {
        NBTTagList tagList = TagUtil.getModifiersTagList(blade);
        int index = TinkerUtil.getIndexInCompoundList(tagList, identifier);
        NBTTagCompound modifierTag = tagList.getCompoundTagAt(index);

        ToolLevelNBT data = getLevelData(modifierTag);
        data.xp += amount;

        if(!canLevelUp(data.level)) {
            return;
        }

        int xpForLevelup = getXpForLevelup(data.level);

        boolean leveledUp = false;
        if(data.xp >= xpForLevelup) {
            data.xp -= xpForLevelup;
            data.level++;
            data.bonusModifiers++;
            leveledUp = true;
        }

        data.write(modifierTag);
        TagUtil.setModifiersTagList(blade, tagList);

        if(leveledUp) {
            this.apply(blade);
            if(!player.world.isRemote) {
                TinkerToolLeveling.proxy.playLevelupDing(player);
                TinkerToolLeveling.proxy.sendLevelUpMessage(data.level, blade, player);
            }
            try {
                NBTTagCompound rootTag = TagUtil.getTagSafe(blade);
                SlashBladeBuilder.rebuildTool(rootTag, (SlashBladeCore)blade.getItem());
                blade.setTagCompound(rootTag);
            } catch(TinkerGuiException e) {
                e.printStackTrace();
            }
        }
    }

    public int getXpForLevelup(int level) {
        if(level <= 1) {
            return getBaseXp();
        }
        return (int) ((float) getXpForLevelup(level - 1) * getLevelMultiplier());
    }

    private ToolLevelNBT getLevelData(ItemStack itemStack) {
        return getLevelData(TinkerUtil.getModifierTag(itemStack, getModifierIdentifier()));
    }

    private ToolLevelNBT getLevelData(NBTTagCompound modifierNBT) {
        return new ToolLevelNBT(modifierNBT);
    }

    public static int getBaseXp() {
        return baseXP;
    }

    public static float getLevelMultiplier() {
        return levelMultiplier;
    }

    public static int getStartingModifiers() {
        return newMinModifiers;
    }

    public static boolean canLevelUp(int currentLevel) {
        return maximumLevels < 0 || maximumLevels >= currentLevel;
    }

    private static class Tooltips {

        public static void addTooltips(ItemStack itemStack, List<String> tooltips) {
            NBTTagCompound tag = TinkerUtil.getModifierTag(itemStack, modLeveling.getModifierIdentifier());
            if(!tag.hasNoTags()) {
                ToolLevelNBT data = new ToolLevelNBT(tag);
                if(canLevelUp(data.level)) {
                    tooltips.add(1, getXpToolTip(data.xp, modLeveling.getXpForLevelup(data.level)));
                }
                tooltips.add(1, getLevelTooltip(data.level));
            }
        }

        private static String getXpToolTip(int xp, int xpNeeded) {
            return String.format("%s: %s", I18n.translateToLocal("tooltip.xp"), getXpString(xp, xpNeeded));
        }

        private static String getXpString(int xp, int xpNeeded) {
            return TextFormatting.WHITE + String.format("%d / %d", xp, xpNeeded);
        }

        private static String getLevelTooltip(int level) {
            return String.format("%s: %s", I18n.translateToLocal("tooltip.level"), getLevelString(level));
        }

        public static String getLevelString(int level) {
            return getLevelColor(level) + getRawLevelString(level) + TextFormatting.RESET;
        }

        private static String getRawLevelString(int level) {
            if(level <= 0) {
                return "";
            }

            if(I18n.canTranslate("tooltip.level." + level)) {
                return I18n.translateToLocal("tooltip.level." + level);
            }

            int i = 1;
            while(I18n.canTranslate("tooltip.level." + i)) {
                i++;
            }

            StringBuilder str = new StringBuilder(I18n.translateToLocal("tooltip.level." + (level % i)));

            for(int j = level / i; j > 0; j--) {
                str.append('+');
            }

            return str.toString();
        }

        private static String getLevelColor(int level) {
            float hue = (0.277777f * level);
            hue = hue - (int) hue;
            return CustomFontColor.encodeColor(Color.HSBtoRGB(hue, 0.75f, 0.8f));
        }
    }
}
