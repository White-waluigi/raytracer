package render;

import ray.Vec2;
import ray.Vec3;

public abstract class Material {
	RenderObject parent;
	
	public static class MaterialProperty{
		public MaterialProperty(Vec3 normal) {
			diffuse=normal;
		}
		public MaterialProperty() {
			
		}
		public  Vec3 diffuse=new Vec3(0);
		public float emissive=(.005f);
		
		public float roughness=(float) .05;
		public  float metallic=(float)0.85;
		
		
	}
	
	public abstract MaterialProperty  get(Vec2 uv);
	
}
