package render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import ray.MathUtilities;
import ray.Vec2;
import ray.Vec3;
import ray.Vec4;
import render.Material.MaterialProperty;

public class Scene {
	public List<RenderObject> objs = new ArrayList<>();
	BSVNode root = null;

	float ambient = 0.06f;
	// Constant-Linear-Quadratic Falloff
	public Vec4 attenuation = new Vec4(9, 4, 90, 1000);
	public Vec3 mainLight = new Vec3(0, 19, 8);
	float shadowSoftness = 0.01f;

	public void add(RenderObject r) {
		r.parent = this;
		objs.add(r);
	}

	public Scene(Vec3 mainLight) {
		if (mainLight != null)
			this.mainLight = mainLight;

		// add(new Sphere(mainLight,.1f,new SolidColorMaterial(new Vec3(1000f)) ));
	}

	public static class Combo {
		RenderObject r;
		Ray ray;

	}

	// Check for intersection in BVS Tree
	public Combo checkIntersect(Ray ray) {

		Combo retv = root.intersect(ray);

		return retv;
	}

	public void optimize() {
		root = (BSVNode.makeTree(objs));

	}

	// Start of new Ray
	public Ray checkIntersectColor(Ray ray) {

		Combo c = checkIntersect(ray);

		// If no intersection, return void color
		if (c.r == null) {
			Ray t = new Ray(new Vec3(Float.MAX_VALUE), new Vec3(Float.MAX_VALUE));
			t.light = new Vec3(Color.DARK_GRAY);
			return t;
		}

		// Calculate Light for Intersection
		c.r.calcLight(c.ray, c.r.getUV(c.ray), ray, c.ray.normal);
		return c.ray;

	}

	// Calculate Ambient Light, Specular Light and diffuse Light
	// Uses Constant-Linear-Quadratic Falloff
	public Vec3 calcLight(Ray ret, MaterialProperty prop, Vec3 normal, Vec3 camera) {

		// Constant Light = Ambient*Diffuse
		Vec3 constLight = (prop.diffuse.scale(ambient + prop.emissive));

		// Get vector between impact and light
		Vec3 dist = mainLight.subtract(ret.pos);

		// Create new Ray for shadow testing
		Ray r = new Ray(ret.pos, dist.normalize().add(Vec3.rand(shadowSoftness)).normalize());

		// make sure to avoid start-point-intersection
		r.pos = r.pos.add(r.dir.scale(0.001f));

		// Check for intersection without calculating color
		Combo c = checkIntersect(r);

		// If no intersection, just use Ambient Light (and reflection)
		if (c.r != null && c.ray.pos.subtract(ret.pos).length() < (dist.length())) {
			return constLight;
		}

		// Caclulate Ambient Lighting
		float g = Math.max(dist.normalize().dot(normal), 0);
		g = (g * calcLightIntensity(dist.length()));
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

	// Calculate falloff
	public float calcLightIntensity(float x) {
		return (float) (attenuation.w / (x * Math.pow(attenuation.x, 2) + x * attenuation.y + attenuation.z));

	}

}
