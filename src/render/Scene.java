package render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import ray.MathUtilities;
import ray.Vec2;
import ray.Vec3;
import ray.Vec4;
import render.Material.MaterialProperty;
import render.lighting.Fullbright;
import render.lighting.Lighting;
import render.lighting.PathTracer;
import render.lighting.RayTracer;

public class Scene {
	public List<RenderObject> objs = new ArrayList<>();
	BSVNode root = null;
	
	public float ambient = 0.06f;
	// Constant-Linear-Quadratic Falloff
	public Vec4 attenuation = new Vec4(9, 4, 90, 1000);
	public Vec3 mainLight = new Vec3(0, 19, 8);
	public float shadowSoftness = 0.01f;

	public Color backColor=Color.BLACK;
	public Lighting light=null;

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
		public RenderObject r;
		public Ray ray;

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
			t.light = new Vec3(backColor);
			return t;
		}

		// Calculate Light for Intersection
		light.calcLight(c.ray, c.r.getUV(c.ray), ray, c.ray.normal,c.r,this);
		return c.ray;

	}

	// Calculate Ambient Light, Specular Light and diffuse Light
	// Uses Constant-Linear-Quadratic Falloff
	public Vec3 calcLight(Ray ret, MaterialProperty prop, Vec3 normal, Vec3 camera) {
		//return light.calcLight(ret, prop, normal, camera);
		throw new RuntimeErrorException(null);
	}

	// Calculate falloff
	public float calcLightIntensity(float x) {
		return (float) (attenuation.w / (x * Math.pow(attenuation.x, 2) + x * attenuation.y + attenuation.z));

	}

}
