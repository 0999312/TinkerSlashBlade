package cn.mmf.slashblade_tic.blade;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import mods.flammpfeil.slashblade.util.ResourceLocationRaw;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.common.ClientProxy;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.IToolStationDisplay;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.library.utils.TooltipBuilder;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.traits.InfiTool;

public class SlashBladeCore extends SlashBladeTICBasic implements IToolStationDisplay {

	public final static int DEFAULT_MODIFIERS = 3;
	
	 public SlashBladeCore(PartMaterialType... requiredComponents) {
		super(requiredComponents);
        this.setCreativeTab(TinkerRegistry.tabTools);
	}

		public List<ResourceLocationRaw> getMuitlModelTexture(ItemStack blade) {
			List<Material> materials = TinkerUtil.getMaterialsFromTagList(TagUtil.getBaseMaterialsTagList(blade));
			List<ResourceLocationRaw> res = new ArrayList<ResourceLocationRaw>();
			res.add(new ResourceLocationRaw("flammpfeil.slashblade","model/handle/" + materials.get(0) + "_handle.png"));
			res.add(new ResourceLocationRaw("flammpfeil.slashblade","model/blade/" + materials.get(1) + "_blade.png"));
			res.add(new ResourceLocationRaw("flammpfeil.slashblade","model/sheath/" + materials.get(2) + "_saya.png"));
			return res;
		}
		
	 
	@Override
	  public List<String> getInformation(ItemStack stack) {
	    return getInformation(stack, true);
	  }

	  @Override
	  public void getTooltip(ItemStack stack, List<String> tooltips) {
	    if(ToolHelper.isBroken(stack)) {
	      tooltips.add("" + TextFormatting.DARK_RED + TextFormatting.BOLD + getBrokenTooltip(stack));
	    }
	    super.getTooltip(stack, tooltips);
	  }

	  protected String getBrokenTooltip(ItemStack itemStack) {
	    return Util.translate(TooltipBuilder.LOC_Broken);
	  }

	  @Override
	  public void getTooltipDetailed(ItemStack stack, List<String> tooltips) {
	    tooltips.addAll(getInformation(stack, false));
	  }

	  public List<String> getInformation(ItemStack stack, boolean detailed) {
	    TooltipBuilder info = new TooltipBuilder(stack);

	    info.addDurability(!detailed);
	    if(hasCategory(Category.HARVEST)) {
	      info.addHarvestLevel();
	      info.addMiningSpeed();
	    }
	    if(hasCategory(Category.LAUNCHER)) {
	      info.addDrawSpeed();
	      info.addRange();
	      info.addProjectileBonusDamage();
	    }
	    info.addAttack();

	    if(ToolHelper.getFreeModifiers(stack) > 0) {
	      info.addFreeModifiers();
	    }

	    if(detailed) {
	      info.addModifierInfo();
	    }

	    return info.getTooltip();
	  }

	  @Override
	  public void getTooltipComponents(ItemStack stack, List<String> tooltips) {
	    List<Material> materials = TinkerUtil.getMaterialsFromTagList(TagUtil.getBaseMaterialsTagList(stack));
	    List<PartMaterialType> component = getRequiredComponents();

	    if(materials.size() < component.size()) {
	      return;
	    }

	    for(int i = 0; i < component.size(); i++) {
	      PartMaterialType pmt = component.get(i);
	      Material material = materials.get(i);

	      // get (one possible) toolpart used to craft the thing
	      Iterator<IToolPart> partIter = pmt.getPossibleParts().iterator();
	      if(!partIter.hasNext()) {
	        continue;
	      }

	      IToolPart part = partIter.next();
	      ItemStack partStack = part.getItemstackWithMaterial(material);
	      if(partStack != null) {
	        // we have the part, add it
	        tooltips.add(material.getTextColor() + TextFormatting.UNDERLINE + partStack.getDisplayName());

	        Set<ITrait> usedTraits = Sets.newHashSet();
	        // find out which stats and traits it contributes and add it to the tooltip
	        for(IMaterialStats stats : material.getAllStats()) {
	          if(pmt.usesStat(stats.getIdentifier())) {
	            tooltips.addAll(stats.getLocalizedInfo());
	            for(ITrait trait : pmt.getApplicableTraitsForMaterial(material)) {
	              if(!usedTraits.contains(trait)) {
	                tooltips.add(material.getTextColor() + trait.getLocalizedName());
	                usedTraits.add(trait);
	              }
	            }
	          }
	        }
	        tooltips.add("");
	      }
	    }
	  }

	  @Nonnull
	  @SideOnly(Side.CLIENT)
	  @Override
	  public FontRenderer getFontRenderer(ItemStack stack) {
	    return ClientProxy.fontRenderer;
	  }

	  @SideOnly(Side.CLIENT)
	  @Override
	  public boolean hasEffect(ItemStack stack) {
	    return TagUtil.hasEnchantEffect(stack);
	  }

	  @Nonnull
	  @Override
	  public String getItemStackDisplayName(@Nonnull ItemStack stack) {
	    // if the tool is not named we use the repair tools for a prefix like thing
	    List<Material> materials = TinkerUtil.getMaterialsFromTagList(TagUtil.getBaseMaterialsTagList(stack));
	    // we save all the ones for the name in a set so we don't have the same material in it twice
	    Set<Material> nameMaterials = Sets.newLinkedHashSet();

	    for(int index : getRepairParts()) {
	      if(index < materials.size()) {
	        nameMaterials.add(materials.get(index));
	      }
	    }

	    return Material.getCombinedItemName(super.getItemStackDisplayName(stack), nameMaterials);
	  }

	  // Creative tab items
	  @Override
	  public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
	    if(this.isInCreativeTab(tab)) {
	      addDefaultSubItems(subItems);
	    }
	  }
//
//	  @Override
//	  public final NBTTagCompound buildTag(List<Material> materials) {
//	    return buildTagData(materials).get();
//	  }
	  
	  protected void addDefaultSubItems(List<ItemStack> subItems, Material... fixedMaterials) {
	    for(Material head : TinkerRegistry.getAllMaterials()) {
	      List<Material> mats = new ArrayList<>(requiredComponents.length);

	      for(int i = 0; i < requiredComponents.length; i++) {
	        if(fixedMaterials.length > i && fixedMaterials[i] != null && requiredComponents[i].isValidMaterial(fixedMaterials[i])) {
	          mats.add(fixedMaterials[i]);
	        }
	        else {
	          // todo: check for applicability with stats
	          mats.add(head);
	        }
	      }

	      ItemStack tool = buildItem(mats);
	      // only valid ones
	      if(hasValidMaterials(tool)) {
	        subItems.add(tool);
	        if(!Config.listAllMaterials) {
	          break;
	        }
	      }
	    }
	  }

	  protected void addInfiTool(List<ItemStack> subItems, String name) {
	    ItemStack tool = getInfiTool(name);
	    if(hasValidMaterials(tool)) {
	      subItems.add(tool);
	    }
	  }

	  protected ItemStack getInfiTool(String name) {
	    // The InfiHarvester!
	    List<Material> materials = ImmutableList.of(TinkerMaterials.slime, TinkerMaterials.cobalt, TinkerMaterials.ardite, TinkerMaterials.ardite);
	    materials = materials.subList(0, requiredComponents.length);
	    ItemStack tool = buildItem(materials);
	    tool.setStackDisplayName(name);
	    InfiTool.INSTANCE.apply(tool);

	    return tool;
	  }

	


	@Override
	protected ToolNBT buildTagData(List<Material> materials) {
		// TODO Auto-generated method stub
		return null;
	}

	 /** The tools name completely without material information */
	  @Override
	  public String getLocalizedToolName() {
	    return Util.translate(getUnlocalizedName() + ".name");
	  }

	  /** The tools name with the given material. e.g. "Wooden Pickaxe" */
	  public String getLocalizedToolName(Material material) {
	    return material.getLocalizedItemName(getLocalizedToolName());
	  }

	  /** Multiplier for damage from materials. Should be fixed per tool. */
	  public float damagePotential() {
		return 0;
	}

	  /**
	   * A fixed damage value where the calculations start to apply dimishing returns.
	   * Basically if you'd hit more than that damage with this tool, the damage is gradually reduced depending on how much the cutoff is exceeded.
	   */
	  public float damageCutoff() {
	    return 15.0f; // in general this should be sufficient and only needs increasing if it's a stronger weapon
	    // fun fact: diamond sword with sharpness V has 15 damage
	  }

	  /**
	   * Allows you set the base attack speed, can be changed by modifiers. Equivalent to the vanilla attack speed.
	   * 4 is equal to any standard item. Value has to be greater than zero.
	   */
	  public double attackSpeed() {
		return 0;
	}

	  /**
	   * Builds a default tool from:
	   * 1. Handle
	   * 2. Head
	   * 3. Accessoire (if present)
	   */
	  protected ToolNBT buildDefaultTag(List<Material> materials) {
	    ToolNBT data = new ToolNBT();

	    if(materials.size() >= 2) {
	      HandleMaterialStats handle = materials.get(0).getStatsOrUnknown(MaterialTypes.HANDLE);
	      HeadMaterialStats head = materials.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
	      // start with head
	      data.head(head);

	      // add in accessoires if present
	      if(materials.size() >= 3) {
	        ExtraMaterialStats binding = materials.get(2).getStatsOrUnknown(MaterialTypes.EXTRA);
	        data.extra(binding);
	      }

	      // calculate handle impact
	      data.handle(handle);
	    }

	    // 3 free modifiers
	    data.modifiers = DEFAULT_MODIFIERS;

	    return data;

	  	}
	  }
