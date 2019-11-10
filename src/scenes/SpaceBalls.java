package scenes;

import java.awt.Color;
import java.io.IOException;

import ray.Vec3;
import render.AxisPlane;
import render.DiffuseTexturedMaterial;
import render.Scene;
import render.SolidColorMaterial;
import render.Sphere;


//Puts a room with a map, a globe and a sun and some other stuff on the screen
public class SpaceBalls implements SceneTemplate{

	@Override
	public Scene load() {
		Scene s = new Scene(null);
		try {
			s.add(new Sphere(new Vec3(-3, 17, 3), 1, Color.green));
			s.add(new Sphere(new Vec3(-5, 14, 10), -1, Color.blue));
			s.add(new Sphere(new Vec3(-1, 30, 6), 5, "world.jpg"));
			
			DiffuseTexturedMaterial dm=new DiffuseTexturedMaterial("rain.jpg");
			dm.glow=true;
			
			s.add(new Sphere(new Vec3(-5, 30, -19), 10, dm));
			s.add(new Sphere(new Vec3(4, 13, -1), 1, Color.lightGray));
			s.add(new Sphere(new Vec3(-9, 110, 5), 1, Color.magenta));

			s.add(new Sphere(new Vec3(0, 10, 5), 1f, "carpet.jpg"));

			DiffuseTexturedMaterial c = new DiffuseTexturedMaterial("sun.jpg");
			c.glow = true;

			Sphere sun = new Sphere(new Vec3(0, 4000, 1000), 1000, c);

			s.add(sun);
			s.add(new Sphere(new Vec3(0, -10, 0), 11, "smile.jpg"));

			s.add(new AxisPlane(AxisPlane.Axis.Y, 30, new DiffuseTexturedMaterial("zelda.jpg"), 10));
			s.add(new AxisPlane(AxisPlane.Axis.Z, 10, new SolidColorMaterial(Color.red), 30));
			s.add(new AxisPlane(AxisPlane.Axis.X, 10, new SolidColorMaterial(Color.green), 30));

			s.add(new AxisPlane(AxisPlane.Axis.Z, -10, new SolidColorMaterial(Color.blue), 30));
			s.add(new AxisPlane(AxisPlane.Axis.X, -10, new SolidColorMaterial(Color.yellow), 30));
			
		} catch (Exception e) {
		}

		return s;
	}

}
