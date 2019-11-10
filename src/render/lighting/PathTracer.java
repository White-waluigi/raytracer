package render.lighting;

import java.awt.Color;

import ray.Vec2;
import ray.Vec3;
import render.Material.MaterialProperty;
import render.Ray;
import render.RenderObject;
import render.Scene;

public class PathTracer implements Lighting{

	public PathTracer() {
		
	}

	private Vec3 calcLightVal(Ray ret, MaterialProperty mp, Vec3 normal, Vec3 pos) {
		return mp.diffuse.scale(mp.emissive);
	}

	@Override
	public void calcLight(Ray ret, Vec2 uv, Ray prev, Vec3 normal, RenderObject ro,Scene parent) {

		ret.bounces=prev.bounces+1;

		
		MaterialProperty mp = ro.material.get(uv);

		
		//ret.dir=prev.dir.reflect(normal);
		//ret.dir=ret.normal.pitch((float) ((Math.random()*2-1)*Math.PI)).yaw((float) ((Math.random()*2-1)*Math.PI)).normalize();
		

		Vec3 self=calcLightVal(ret,mp/*new MaterialProperty(normal)*/,normal,prev.pos);
		ret.light=self;
		
		
		
		

		 
		//Send new Ray on its Way
		if(!(prev.bounces>=RenderObject.MAX_BOUNCE)) {
			
			Vec3 refl;
			do {
				refl=new Vec3(Math.random()-.5,Math.random()-.5,Math.random()-.5).normalize();
			}while(refl.dot(ret.normal)<0);
			
			Ray next=new Ray(ret.pos,refl);
			next.pos=next.pos.add(next.dir.scale(.001f));
			
			next.bounces=ret.bounces;		
			//recursion is here
			ret.light=ro.parent.checkIntersectColor(next).light;
			
			
			float p = (float) (1/(2*Math.PI));
			float cos_theta = next.dir.dot(ret.normal);
			Vec3 BRDF = mp.diffuse.clip(1.0f).scale((float) (1f/Math.PI));
			 
			ret.light= self.add(BRDF.mul(ret.light.clip(2.0f)).scale( cos_theta / p)  );
		}
	}
//
//	Color TracePath(Ray ray, count depth) {
//
//	    if (depth >= MaxDepth) {
//
//	      return Black;  // Bounced enough times.
//
//	    }
//
//
//	    ray.FindNearestObject();
//
//	    if (ray.hitSomething == false) {
//
//	      return Black;  // Nothing was hit.
//
//	    }
//
//
//	    Material material = ray.thingHit->material;
//
//	    Color emittance = material.emittance;
//
//
//	    // Pick a random direction from here and keep going.
//
//	    Ray newRay;
//
//	    newRay.origin = ray.pointWhereObjWasHit;
//
//
//	    // This is NOT a cosine-weighted distribution!
//
//	    newRay.direction = RandomUnitVectorInHemisphereOf(ray.normalWhereObjWasHit);
//
//
//	    // Probability of the newRay
//
//	    const float p = 1/(2*M_PI);
//
//
//	    // Compute the BRDF for this ray (assuming Lambertian reflection)
//
//	    float cos_theta = DotProduct(newRay.direction, ray.normalWhereObjWasHit);
//
//	    Color BRDF = material.reflectance / M_PI ;
//
//
//	    // Recursively trace reflected light sources.
//
//	    Color incoming = TracePath(newRay, depth + 1);
//
//
//	    // Apply the Rendering Equation here.
//
//	    return emittance + (BRDF * incoming * cos_theta / p);
//
//	  }
//
//
//	  void Render(Image finalImage, count numSamples) {
//
//	    foreach (pixel in finalImage) {
//
//	      foreach (i in numSamples) {
//
//	        Ray r = camera.generateRay(pixel);
//
//	        pixel.color += TracePath(r, 0);
//
//	      }
//
//	      pixel.color /= numSamples;  // Average samples.
//
//	    }
//
//	  }
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
