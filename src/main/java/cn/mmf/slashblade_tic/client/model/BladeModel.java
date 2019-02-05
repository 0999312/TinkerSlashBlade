package cn.mmf.slashblade_tic.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import cn.mmf.slashblade_tic.blade.ItemSlashBladeTIC;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.client.model.obj.Face;
import mods.flammpfeil.slashblade.client.model.obj.WavefrontObject;
import mods.flammpfeil.slashblade.event.ModelRegister;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import mods.flammpfeil.slashblade.util.ResourceLocationRaw;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Color3b;
import javax.vecmath.Color4b;
import javax.vecmath.Color4f;
import javax.vecmath.Matrix4f;
import java.awt.*;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by Furia on 2016/02/07.
 */
public class BladeModel implements IBakedModel {

    List<BakedQuad> emptyList = null;
    List<BakedQuad> getEmptyList(){
        if(emptyList == null)
            emptyList = Lists.newArrayList(new BakedQuad(new int[28], 0, EnumFacing.UP, getParticleTexture(), false, net.minecraft.client.renderer.vertex.DefaultVertexFormats.ITEM));
        return emptyList;
    }

    ItemStack proudsoul = ItemStack.EMPTY;
    ItemModelMesher modelMesher = null;
    List<BakedQuad> getDefaultQuards(){
        if(modelMesher == null) {
            modelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
            proudsoul = SlashBlade.findItemStack(SlashBlade.modid, SlashBlade.ProudSoulStr, 1);
        }
        return modelMesher.getItemModel(proudsoul).getQuads(null,null,0);
    }

    static ItemStack targetStack = ItemStack.EMPTY;
    static ItemSlashBladeTIC itemBlade = null;
    static EntityLivingBase user = null;

    /**
     * 0 : type set
     * 1 : texture set
     */
    static int renderPath = 0;
    static int drawStep = -1;

    static ItemCameraTransforms.TransformType type = ItemCameraTransforms.TransformType.NONE;


    @Override
    public ItemOverrideList getOverrides() {
        return new ItemOverrideList(ImmutableList.<ItemOverride>of()){
            @Override
            public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {

                if(stack != null && stack.getItem() instanceof ItemSlashBladeTIC) {
                    targetStack = stack;
                    ItemSlashBlade.getItemTagCompound(targetStack).setBoolean("IsRender",true);
                    itemBlade = (ItemSlashBladeTIC) stack.getItem();
                    user = entity == null ? user : entity;
                }else{
                    targetStack = ItemStack.EMPTY;
                    itemBlade = null;
                    user = null;
                }

                return super.handleItemState(originalModel, stack, world, entity);
            }

        };
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        if(side != null)
            return getEmptyList();

        if(drawStep == 0) return getDefaultQuards();

        return getEmptyList();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        drawStep = 1;
        renderPath = 0;

        return true;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(SlashBlade.proudSoul);
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return new ItemCameraTransforms(ItemCameraTransforms.DEFAULT){
            @Override
            public ItemTransformVec3f getTransform(TransformType srctype) {
                type = srctype;
                return super.getTransform(srctype);
            }
        } ;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        this.type = cameraTransformType;

        drawStep = 0;
        return PerspectiveMapWrapper.handlePerspective(this, ModelRotation.X0_Y0, cameraTransformType);
    }
}
