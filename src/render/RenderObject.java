package render;

import java.awt.Color;

import ray.Vec2;
import ray.Vec3;
import ray.Vec4;
import render.Material.MaterialProperty;


//Generic Rendarable Object.
//Also takes care of recursive rays
public abstract class RenderObject implements BSVObject{
	//Limit of Ray bounce reflections
	public static final int MAX_BOUNCE=3;
	public Vec3 col=new Vec3(Color.red);
	public Scene parent;
	public Material material=null;

	//Check for intersection and return normal+impact point
	public abstract Ray intersect(Ray ray);
	//Get UV Coords for Material Calculation
	public abstract Vec2 getUV(Ray ray);


	public void calcLight(Ray ret,Vec2 uv,Ray prev,Vec3 normal) {
		
		
		if(material==null) {
			material=new SolidColorMaterial(col);
		}
		
		ret.bounces=prev.bounces+1;
		MaterialProperty mp = material.get(uv);
		Vec3 self=parent.calcLight(ret,mp/*new MaterialProperty(normal)*/,normal,prev.pos);
		ret.light=self;
		ret.dir=prev.dir.reflect(normal);
		ret.dir=ret.dir.add(Vec3.bias(mp.roughness) );
		
		
		//Send new Ray on its Way
		if(!(prev.bounces>=MAX_BOUNCE)) {

			Ray next=new Ray(ret.pos,ret.dir);
			next.pos=next.pos.add(next.dir.scale(.001f));
			
			next.bounces=ret.bounces;		
			//recursion is here
			ret.light=parent.checkIntersectColor(next).light;
			ret.light=ret.light.lerp(self, 1-mp.metallic);
		}

	}

}
