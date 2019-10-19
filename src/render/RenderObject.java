package render;

import java.awt.Color;

import ray.Vec2;
import ray.Vec3;
import ray.Vec4;
import render.Material.MaterialProperty;

public abstract class RenderObject implements BSVObject{
	public static final int MAX_BOUNCE=15;
	Vec3 col=new Vec3(Color.red);
	public Scene parent;
	Material material=null;

	public abstract Ray intersect(Ray ray);
	public abstract Vec2 getUV(Ray ray);


	public void calcLight(Ray ret,Vec2 uv,Ray prev,Vec3 normal) {
		if(material==null) {
			material=new SolidColorMaterial(col);
		}
		ret.bounces=prev.bounces+1;
		MaterialProperty mp = material.get(uv);
		Vec3 self=parent.calcLight(ret,mp/*new MaterialProperty(normal)*/,normal);
		ret.light=self;
		ret.dir=prev.dir.reflect(normal);
		//ret.dir=ret.dir.add(new Vec3(Math.random()*mp.roughness,Math.random()*mp.roughness,Math.random()*mp.roughness));
		
		if(!(prev.bounces>=MAX_BOUNCE)) {

			Ray next=new Ray(ret.pos,ret.dir);
			next.pos=next.pos.add(next.dir.scale(.001f));
			
			next.bounces=ret.bounces;		
			ret.light=parent.checkIntersectColor(next).light;
			ret.light=ret.light.lerp(self, mp.metallic);
		}

	}

}
