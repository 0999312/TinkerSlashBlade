package cn.mmf.slashblade_tic.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;

import org.apache.commons.io.IOUtils;

import mods.flammpfeil.slashblade.util.ResourceLocationRaw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class TextureMixer {

	   public static BufferedImage TextureMix(List<ResourceLocationRaw> list,float alpha) throws IOException {
	        // 获取List并读取
		   	IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
		   	List<BufferedImage> imgs = new ArrayList<BufferedImage>();
	        BufferedImage buffImg = new BufferedImage(64, 128, BufferedImage.TYPE_4BYTE_ABGR);
	        
	        for(int i = 0; i<list.size();i++){
	        	if( manager.getResource(list.get(i))!=null){
	        	InputStream imageStream = manager.getResource(list.get(i)).getInputStream();
	        	try
	            {
	        		imgs.add(ImageIO.read(imageStream));
	            }
	            finally
	            {
	                IOUtils.closeQuietly(imageStream);
	            }
	        	}else{
	        		imgs.add(new BufferedImage(64, 128, BufferedImage.TYPE_4BYTE_ABGR));
	        	}
	        }
	        
	        // 创建Graphics2D对象，用在底图对象上绘图
            Graphics2D g2d = buffImg.createGraphics();  
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, alpha));
	        g2d.drawImage(imgs.get(0), 0, 0, imgs.get(0).getWidth(), imgs.get(0).getHeight(), null);
	        g2d.drawImage(imgs.get(1), 0, 64, imgs.get(1).getWidth(), imgs.get(1).getHeight(), null);
	        g2d.drawImage(imgs.get(2), 0, 96, imgs.get(2).getWidth(), imgs.get(2).getHeight(), null);
	        g2d.dispose();// 释放图形上下文使用的系统资源
	        return buffImg;
	    }
	   
	   public static ResourceLocationRaw  generateTexture(BufferedImage buffImg) {
		   ResourceLocation res =  Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("test_texture", new DynamicTexture(buffImg));
		   ResourceLocationRaw res1 = new ResourceLocationRaw(res.getResourceDomain(), res.getResourcePath());
		return res1;
	    }

}
