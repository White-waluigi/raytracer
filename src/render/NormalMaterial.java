package render;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ray.Vec2;
import ray.Vec3;
import render.Material.MaterialProperty;

public class NormalMaterial extends Material {

	public NormalMaterial(Object ... blue) {
		super();

	}
	@Override
	public MaterialProperty get(Vec2 uv) {

		MaterialProperty ret=new MaterialProperty();
		ret.diffuse= uv.xy0();
		return ret;
	}
}
