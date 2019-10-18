package render;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import ray.Vec2;
import ray.Vec3;
import render.Material.MaterialProperty;

public class DiffuseTexturedMaterial extends Material {
	static Map<String, Raster> loaded=new HashMap<>();
	
	Raster raster;
	public DiffuseTexturedMaterial(String img) throws IOException {
		

		 if(!loaded.containsKey(img)) {
			 File imgPath = new File(img);
			 BufferedImage bufferedImage = ImageIO.read(imgPath);
			 
			 loaded.put(img, bufferedImage.getRaster());
		 }
		 
		 raster = loaded.get(img);
	}
	@Override
	public MaterialProperty get(Vec2 uv) {
		float[] t=null;

		int x=(int)(Math.abs(uv.x%1)*raster.getWidth());
		int y=(int)(Math.abs(uv.y%1)*raster.getHeight());
		Vec3 col= new Vec3((raster.getPixel(x,y,t))).scale(1.f/256);

		MaterialProperty ret=new MaterialProperty();
		ret.diffuse=col;
		return ret;
	}


}

