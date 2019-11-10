package render;

import java.awt.Color;

import ray.Vec2;
import ray.Vec3;


//Single color material
public class SolidColorMaterial extends Material {
	Vec3 color;
	public double glow=0;
	
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
		mat.emissive=(float) (glow);
		return mat;
	}

}
