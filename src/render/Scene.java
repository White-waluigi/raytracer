package render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import ray.Vec2;
import ray.Vec3;
import ray.Vec4;
import render.Material.MaterialProperty;

public class Scene {
	public List<RenderObject> objs=new ArrayList<>();
	float ambient=.1f;
	Vec4 attenuation=new Vec4(9,5,90,1000);
	Vec3 mainLight=new Vec3(0,19,8);
	float shadowSoftness=0.01f;
	public void add(RenderObject r) {
		r.parent=this;
		objs.add(r);
		
		
	}
	public Scene() {
		//add(new Sphere(mainLight,.1f,new SolidColorMaterial(new Vec3(1000f))  ));
	}
	class Combo{
		RenderObject r;		
		Ray ray;
		
	}
	public Combo checkIntersect(Ray ray) {
		
		Combo retv = new Combo();
		Vec3 shortest=new Vec3(Float.MAX_VALUE);
		Ray c=new Ray(shortest, shortest);
		retv.ray=c;
		for(RenderObject o:objs) {
			Ray ret=o.intersect(ray);
			if (ret != null && shortest.subtract(ray.pos).length()>ret.pos.subtract(ray.pos).length()) {
				shortest=ret.pos;
				retv.ray=ret;
				retv.r=o;
				
				
			}
			
		}
		
		return retv;
	}
	public Ray checkIntersectColor(Ray ray) {
		
		//Vec3 shortest=new Vec3(Float.MAX_VALUE);
		//Ray c=new Ray(shortest, shortest);
		
		Combo c=checkIntersect(ray);
		
//		for(RenderObject o:objs) {
//			Ray ret=o.intersect(ray);
//			if (ret != null && shortest.subtract(ray.pos).length()>ret.pos.subtract(ray.pos).length()) {
//				shortest=ret.pos;
//				
//				//float g=Math.max( new Vec3(0.7,-0.4,-1).normalize().dot(ret.dir)  ,0)+ambient;
//				
//				
//				c=ret;
//				
//				
//			}
//			
//		}
		if(c.r==null) {
			Ray t=new Ray(new Vec3(Float.MAX_VALUE),new Vec3(Float.MAX_VALUE));
			t.light=new Vec3(Color.black);
			return t;
		}
		
		
		c.r.calcLight(c.ray,c.r.getUV(c.ray), ray, c.ray.normal);
		return c.ray;
		
	}	
	public Vec3 calcLight(Ray ret,MaterialProperty prop,Vec3 normal) {
		Vec3 dist=mainLight.subtract(ret.pos);
		Ray r=new Ray(ret.pos,dist.normalize().add(Vec3.rand(shadowSoftness)).normalize());
		r.pos=r.pos.add(r.dir.scale(0.001f));
		Combo c=checkIntersect(r);
		
		//System.out.println(c.ray.pos.subtract(ret.pos).length()+","+dist.length());
		if(c.r==null || c.ray.pos.subtract(ret.pos).length()<dist.length()) {
			return  prop.diffuse.scale(ambient);
			
		}
		
		Vec3 lightDist=mainLight.subtract(ret.pos);

		float g=Math.max(lightDist.normalize().dot(normal)  ,0);
		g=(g*calcLightIntensity(lightDist.length()));
		g*=g;
		g+=ambient;
		return prop.diffuse.scale(g);

	}
	
	public float calcLightIntensity(float x) {
		return  (float) (attenuation.w/(x*Math.pow(attenuation.x,2)+x*attenuation.y+attenuation.z));
		
	}
	
}




