package render.lighting;

import ray.MathUtilities;
import ray.Vec2;
import ray.Vec3;
import render.Material.MaterialProperty;
import render.Ray;
import render.RenderObject;
import render.Scene;
import render.SolidColorMaterial;
import render.Scene.Combo;

public class RayTracer implements Lighting{
	
	

	public RayTracer() {
		super();
	}

	public Vec3 calcLightVal(Ray ret, MaterialProperty prop, Vec3 normal, Vec3 camera,Scene parent) {
		
		// Constant Light = Ambient*Diffuse
		Vec3 constLight = (prop.diffuse.scale(parent.ambient + prop.emissive));

		// Get vector between impact and light
		Vec3 dist = parent.mainLight.subtract(ret.pos);

		// Create new Ray for shadow testing
		Ray r = new Ray(ret.pos, dist.normalize().add(Vec3.rand(parent.shadowSoftness)).normalize());

		// make sure to avoid start-point-intersection
		r.pos = r.pos.add(r.dir.scale(0.001f));

		// Check for intersection without calculating color
		Combo c = parent.checkIntersect(r);

		// If no intersection, just use Ambient Light (and reflection)
		if (c.r != null && c.ray.pos.subtract(ret.pos).length() < (dist.length())) {
			return constLight;
		}

		// Caclulate Ambient Lighting
		float g = Math.max(dist.normalize().dot(normal), 0);
		g = (g * parent.calcLightIntensity(dist.length()));
		g *= g;
		
		Vec3 spec=new  Vec3(0);
		if (prop.specular != 0) {
			// Calculate Specular Lighting
			Vec3 viewDir = camera.subtract(ret.pos).normalize();
			Vec3 LightToObjDir = dist.normalize();
			Vec3 h = LightToObjDir.add(viewDir).normalize();

			float specular = (float) Math.pow(MathUtilities.clamp(ret.normal.dot(h)), prop.specular) * 5.f;

			// Specular Lighting always white
			spec = new Vec3(specular).scale(g);
		}
		// Add everything together for final Light value
		return prop.diffuse.scale(g).add(constLight).add(spec);
	}
	public void calcLight(Ray ret,Vec2 uv,Ray prev,Vec3 normal,RenderObject ro,Scene parent) {
		
		
		if(ro.material==null) {
			ro.material=new SolidColorMaterial(ro.col);
		}
		
		ret.bounces=prev.bounces+1;
		MaterialProperty mp = ro.material.get(uv);
		Vec3 self=calcLightVal(ret,mp/*new MaterialProperty(normal)*/,normal,prev.pos,parent);
		ret.light=self;
		ret.dir=prev.dir.reflect(normal);
		ret.dir=ret.dir.add(Vec3.bias(mp.roughness) );
		
		
		//Send new Ray on its Way
		if(!(prev.bounces>=RenderObject.MAX_BOUNCE)) {

			Ray next=new Ray(ret.pos,ret.dir);
			next.pos=next.pos.add(next.dir.scale(.001f));
			
			next.bounces=ret.bounces;		
			//recursion is here
			ret.light=ro.parent.checkIntersectColor(next).light;
			ret.light=ret.light.lerp(self, 1-mp.metallic);
		}

	}	
	public String toString() {
		return this.getClass().getSimpleName();
	}



}
