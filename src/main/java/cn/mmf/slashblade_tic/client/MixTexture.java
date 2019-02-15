package cn.mmf.slashblade_tic.client;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class MixTexture extends AbstractTexture
{
    private static final Logger LOGGER = LogManager.getLogger();
    protected final BufferedImage texture;
    

    public MixTexture(BufferedImage texture)
    {
        this.texture = texture;
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException
    {
        this.deleteGlTexture();
        IResource iresource = null;
        try
        {
            BufferedImage bufferedimage = texture;
            boolean flag = false;
            boolean flag1 = false;
            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage, flag, flag1);
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)iresource);
        }
    }
}