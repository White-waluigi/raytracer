package render;

import java.awt.Color;

import ray.Vec2;
import ray.Vec3;

public class AxisPlane extends RenderObject {

	public enum Axis {
		X,
		Y,
		Z
		
	};
	Axis axis;
	float offset;
	float width;
	
	public AxisPlane(Axis axis, float offset,Material c,float width) {
		super();
		this.axis = axis;
		this.offset = offset;
		material=c;
		this.width=width;
	}


	@Override
	public Ray intersect(Ray ray) {
		//Vec3 v=ray.dir.subtract(ray.pos);
		Vec3 v=ray.dir;
		
		float d=(offset-ray.pos.i(axis.ordinal()))/v.i(axis.ordinal());
		
		Vec3 impact=ray.pos.add(ray.dir.scale(d));
		if( d<0 || ! Vec3.Identity(axis.ordinal()).subtract(impact).AxisRange(axis.ordinal(), width, -width))
			return null;
		
		
//		return new Ray(ray.dir.inv(axis.ordinal()),ray.pos.add(ray.dir.scale(d)));
		
		Ray ret=new Ray(impact,getNormal(ray));
		ret.normal=getNormal(ray);
		//calcLight(ret, impact.xy(axis.ordinal()).scale(.5f/width).add(new Vec2(.5)),ray,getNormal());
		return ret;
		
	}
	public Vec2 getUV(Ray r) {
		return r.pos.xy(axis.ordinal()).scale(.5f/width).add(new Vec2(.5));
		
	}
	public Vec3 getNormal(Ray ray) {
		
		float dist=ray.pos.i(axis.ordinal())-offset;
		
		return Vec3.Identity(axis.ordinal()).scale(Math.signum(dist)).normalize();
	}


	@Override
	public BoundingSphere getBoundingSphere() {
		return new BoundingSphere( Vec3.Identity(axis.ordinal()), (float) (width*Math.sqrt(2)));
	}

}
