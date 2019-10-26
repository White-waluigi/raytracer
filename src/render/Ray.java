package render;

import java.awt.Color;

import ray.Vec3;
import ray.Vec4;

//Holds all the data that a single Ray needs
public class Ray {
	
	public Vec3 dir;
	public Vec3 pos;
	public Vec3 light;
	public int bounces=0;
	public Vec3 normal;
	
	public Ray( Vec3 pos,Vec3 dir,Color light) {
		super();
		this.dir = dir.normalize();
		this.pos = pos;
		this.light=new Vec3(light);
	}
	public Ray( Vec3 pos,Vec3 dir) {
		this(pos,dir,Color.black);
	}
	
}
