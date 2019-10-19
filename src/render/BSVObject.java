package render;

import ray.Vec3;

public interface BSVObject{
	public class BoundingSphere
	{
		public final Vec3 pos;
		public final float rad;
		
		BoundingSphere(Vec3 p,float r){
			this.pos=p;
			this.rad=r;
		}
		float distance(BoundingSphere b) {
			return b.rad+rad+pos.subtract(b.pos).length();
			
			
		}
		public BoundingSphere merge(BoundingSphere bsvObject) {
			return new BoundingSphere( pos.middle(bsvObject.pos),distance(bsvObject));
		}
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
	}
	public BoundingSphere getBoundingSphere();
}