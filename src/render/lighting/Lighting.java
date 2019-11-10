package render.lighting;

import ray.Vec2;
import ray.Vec3;
import render.Ray;
import render.RenderObject;
import render.Scene;
import render.SolidColorMaterial;
import render.Material.MaterialProperty;

public interface Lighting  {
	public void calcLight(Ray ret,Vec2 uv,Ray prev,Vec3 normal,RenderObject ro,Scene p) ;
}
