package render;

import ray.Vec3;

//Either leaf or Node of BSVTree
public interface BSVObject{
	
	//Bounding Sphere to Determine collision
	public class BoundingSphere
	{
		public final Vec3 pos;
		public final float rad;
		
		BoundingSphere(Vec3 p,float r){
			this.pos=p;
			this.rad=r;
		}
		//Distance between two points furthest apart
		float distance(BoundingSphere b) {
			return b.rad+rad+pos.subtract(b.pos).length();
			
			
		}
		//Take two Spheres and return one that encompasses both
		public BoundingSphere merge(BoundingSphere bsvObject) {
			return new BoundingSphere( pos.middle(bsvObject.pos),distance(bsvObject));
		}
		
		//Ray Sphere intersection
		public Ray intersect(Ray ray) {
			Vec3 p=pos.subtract(ray.pos);
			
			Vec3 r=ray.dir.normalize();
			float d=r.dot(p);
			if(d<0)
				return null;
			
			r=r.scale(d);
			float fulllen=r.length();
			r=r.subtract(p);
			if (r.length()<rad) {
				float len=(float) Math.sqrt(rad*rad- r.length()*r.length());
				len=fulllen-len;
				Vec3 impact=ray.dir.scale(len).add(ray.pos);
				
				
				Ray ret= new Ray(impact,ray.dir.reflect(impact.subtract(p).normalize()));
				Vec3 normal=impact.subtract(pos).normalize();
				//calcLight(ret,getUV(normal),ray,normal);
				ret.normal=normal;
				return ret;
			}
			
			return null;
		}
		
		//Check if Ray starts inside Sphere
		public boolean collide(Ray r) {
			if(r.pos.subtract(pos).length()<rad) {
				return true;
			}
			else return intersect(r)!=null;
		}
	}
	public BoundingSphere getBoundingSphere();
}