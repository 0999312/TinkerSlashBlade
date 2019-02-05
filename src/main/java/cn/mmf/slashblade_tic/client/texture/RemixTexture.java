package cn.mmf.slashblade_tic.client.texture;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mods.flammpfeil.slashblade.util.ResourceLocationRaw;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RemixTexture extends AbstractTexture {

	    protected List<ResourceLocationRaw> textureLocation = new ArrayList<ResourceLocationRaw>();

	    public RemixTexture(List<ResourceLocationRaw> textureResourceLocation)
	    {
	        this.textureLocation = textureResourceLocation;
	    }

		public void loadTexture(IResourceManager resourceManager) throws IOException
	    {
	        this.deleteGlTexture();
	        IResource iresource = null;
	        BufferedImage bufferedimage = new BufferedImage(64, 128, BufferedImage.TYPE_INT_ARGB);
	        BufferedImage bufferedimage_final = new BufferedImage(64, 128, BufferedImage.TYPE_INT_ARGB);
	        try
	        {
	        	for(int i =0;i<=textureLocation.size();i++){
	            iresource = resourceManager.getResource(this.textureLocation.get(i));
	            bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());
	            Graphics2D g = bufferedimage_final.createGraphics();
	            g.drawImage(bufferedimage, 0, 0, 64,128, null);
	            g.dispose();
	        	}
	           
	            boolean flag = false;
	            boolean flag1 = false;

	            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage_final, flag, flag1);
	        }
	        finally
	        {
	        	for(int i =0;i<=textureLocation.size();i++){
	            IOUtils.closeQuietly((Closeable)iresource);
	            }
	        }
	    }
}
