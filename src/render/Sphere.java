package render;

import java.awt.Color;
import java.io.IOException;

import ray.SWRenderer;
import ray.Vec2;
import ray.Vec3;
import ray.Vec4;

public class Sphere extends RenderObject{
	Vec3 pos;
	float rad;

	


	public Sphere(Vec3 pos, float d,Material c) {
		super();
		this.pos = pos;
		this.rad = d;
		this.material=c;	

	
	}
	public Sphere(Vec3 pos2, float d, Color s) throws IOException {
		this(pos2,d,new SolidColorMaterial(s));
	}
	public Sphere(Vec3 pos2, float d, String string) throws IOException {
		this(pos2,d,new DiffuseTexturedMaterial(string));
	}

	@Override
	public Ray intersect(Ray ray) {
		return new BoundingSphere(pos,rad).intersect(ray);
	}
	public Vec2 getUV(Ray r) {
		float x=(float) (Math.atan2((r.normal.y),r.normal.x)/(2*Math.PI))+.5f+(SWRenderer.frame*.01f);
		//float y=(float) (Math.atan2((normal.y),normal.z+1)/(2*Math.PI));
		float y=(float) ((Math.asin(r.normal.z)+Math.PI/2f)/(Math.PI));
		return new Vec2(x,y);
		
	}
	@Override
	public BoundingSphere getBoundingSphere() {
		return new BoundingSphere(pos,rad);
	}
}
