package cn.mmf.slashblade_tic.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;

import org.apache.commons.io.IOUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;

import cn.mmf.slashblade_tic.client.MixTexture;
import mods.flammpfeil.slashblade.util.ResourceLocationRaw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.client.RenderUtil;
import slimeknights.tconstruct.library.materials.Material;

@SideOnly(Side.CLIENT)
public class TextureMixer {
	   private static Map<String, BufferedImage[]> imageMap = Maps.newHashMap();
	   private static TextureMixer mixer = new TextureMixer();
	   public static TextureMixer getInstance(){
			return mixer;
	   }

	   public static List<BufferedImage> Rendering(String name,List<Material> materials,List<ResourceLocationRaw> list) throws IOException{
		   IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
		   List<BufferedImage> imgs = new ArrayList<BufferedImage>();
		   BufferedImage[] img ;
		   InputStream imageStream,imageStream2;
	        for(int i = 0; i<list.size()-3;i++){
	        	imageStream = manager.getResource(list.get(i)).getInputStream();
	        	imageStream2 = manager.getResource(list.get(i+3)).getInputStream();
	        	try
					{
	        		img = mixer.useColor(materials.get(i).identifier+"_"+name);
	        		imgs.add(img[i]);
	            }
	            finally
	            {
	                IOUtils.closeQuietly(imageStream);
	                IOUtils.closeQuietly(imageStream2);
	        	}
	        }
		   return imgs;
	   }

	   public static BufferedImage[] useColor(String idn) {
		   if(imageMap.get(idn)!=null)
			   return imageMap.get(idn);
		   else
		   	throw new NullPointerException("Mismatched!");
	   }

	   public static void MapColor(String idn, BufferedImage[] dest){
	   		imageMap.put(idn, dest);
	   }

	public static BufferedImage getOverlay(int color, BufferedImage img2, BufferedImage img_color){
	   	BufferedImage img = new BufferedImage(img2.getWidth(), img2.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.drawImage(img2, 0,0,null);
		Graphics2D g2d1 = img_color.createGraphics();
		g2d1.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC,1F));
        Color color1 = new Color(color);
        int red= color1.getRed()-25; if(red>255) red=255; if(red<0) red=0;
        int green= color1.getGreen()-25; if(green>255) green=255; if(green<0) green=0;
        int blue= color1.getBlue()-25; if(blue>255) blue=255; if(blue<0) blue=0;
        Color color4 = new Color(red,green,blue);
        g2d1.setColor(color4);
		g2d1.fillRect(0,0,img_color.getWidth(), img_color.getHeight());
		g2d1.finalize();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,0.475F));
		g2d.drawImage(img_color, 0, 0, null);
		g2d.finalize();
		
		return img;
	}

	public BufferedImage TextureMix(List<BufferedImage> imgs,float alpha) {
		BufferedImage buffImg = new BufferedImage(64, 128, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = buffImg.createGraphics();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, alpha));
		g2d.drawImage(imgs.get(0), 0, 0, imgs.get(0).getWidth(), imgs.get(0).getHeight(), null);
		g2d.drawImage(imgs.get(1), 0, 64, imgs.get(1).getWidth(), imgs.get(1).getHeight(), null);
		g2d.drawImage(imgs.get(2), 0, 96, imgs.get(2).getWidth(), imgs.get(2).getHeight(), null);
		g2d.finalize();
		return buffImg;
	}

	private final Cache<BufferedImage, ResourceLocation> texCache = CacheBuilder.newBuilder()
			.maximumSize(500L)
			.expireAfterWrite(10L, TimeUnit.MINUTES)
			.build();
	private final Map<String, Integer> mapTextureCounters = Maps.<String, Integer>newHashMap();

	public ResourceLocationRaw  generateTexture(BufferedImage buffImg) throws ExecutionException {
		ResourceLocation res = texCache.get(buffImg,()-> getMixTextureLocation("blade_texture", new MixTexture(buffImg)));
		ResourceLocationRaw res1 = new ResourceLocationRaw(res.getResourceDomain(), res.getResourcePath());
		return res1;
	    }
	   public TextureMixer() {

	   }
	   
	   public ResourceLocation getMixTextureLocation(String name, MixTexture texture)
	    {
	        Integer integer = this.mapTextureCounters.get(name);

	        if (integer == null)
	        {
	            integer = Integer.valueOf(1);
	        }
	        else
	        {
	            integer = integer.intValue() + 1;
	        }

	        this.mapTextureCounters.put(name, integer);
	        ResourceLocation resourcelocation = new ResourceLocation(String.format("mix_texture/%s_%d", name, integer));
	        Minecraft.getMinecraft().getTextureManager().loadTexture(resourcelocation, texture);
	        return resourcelocation;
	    }
}
