package render;

import ray.Vec2;
import ray.Vec3;


//Material property for single impact point
public abstract class Material {
	RenderObject parent;
	
	public static class MaterialProperty{
		public MaterialProperty(Vec3 normal) {
			diffuse=normal;
		}
		public MaterialProperty() {
			
		}
		
		//Diffuse color
		public  Vec3 diffuse=new Vec3(0);
		//Glow Color
		public float emissive=(.00f);
		
		//Fresnel reflection
		public float roughness=(float) 0.05;
		//"Shinyness"
		public  float metallic=(float)0.15;
		public float specular=200f;
		
		
	}
	
	public abstract MaterialProperty  get(Vec2 uv);
	
}
