package render;

import java.awt.Color;

import ray.Vec2;
import ray.Vec3;

public class SolidColorMaterial extends Material {
	Vec3 color;
	
	public SolidColorMaterial(Vec3 color) {
		super();
		this.color = color;
	}
	public SolidColorMaterial(Color color) {
		this(new Vec3(color));
	}

	@Override
	public MaterialProperty get(Vec2 uv) {
		MaterialProperty mat=new MaterialProperty();
		mat.diffuse=color;
		return mat;
	}

}
