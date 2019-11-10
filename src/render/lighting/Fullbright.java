package render.lighting;

import ray.Vec2;
import ray.Vec3;
import render.Ray;
import render.RenderObject;
import render.Scene;

public class Fullbright implements Lighting{

	@Override
	public void calcLight(Ray ret, Vec2 uv, Ray prev, Vec3 normal, RenderObject ro,Scene s) {
		ret.light=ro.material.get(uv).diffuse;
		
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
}
