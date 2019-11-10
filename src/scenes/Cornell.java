package scenes;

import java.awt.Color;

import ray.Vec3;
import render.AxisPlane;
import render.DiffuseTexturedMaterial;
import render.Scene;
import render.SolidColorMaterial;
import render.Sphere;

public class Cornell implements SceneTemplate {

	@Override
	public Scene load() {
		Scene s = new Scene(null);
		try {
//			s.add(new Sphere(new Vec3(-3, 17, 3), 1, Color.green));
//			s.add(new Sphere(new Vec3(-5, 14, 10), -1, Color.blue));
//			s.add(new Sphere(new Vec3(-1, 30, 6), 5, "world.jpg"));
			SolidColorMaterial sm=new SolidColorMaterial(Color.white);
			sm.glow=10.0;
			
			s.add(new Sphere(new Vec3(-2, 25, -19), 10, Color.DARK_GRAY));
			s.add(new Sphere(new Vec3(5, 22, 7.5), 3, Color.YELLOW));
			s.add(new Sphere(new Vec3(-7, 28, 9), 2, sm));
//
//			s.add(new Sphere(new Vec3(0, 10, 5), 1f, "carpet.jpg"));
//
//			DiffuseTexturedMaterial c = new DiffuseTexturedMaterial("sun.jpg");
//			c.glow = true;
//
//			Sphere sun = new Sphere(new Vec3(0, 4000, 1000), 1000, c);
//
//			s.add(sun);
//			s.add(new Sphere(new Vec3(0, -10, 0), 11, "smile.jpg"));

			DiffuseTexturedMaterial d= new DiffuseTexturedMaterial("zelda.jpg");
			
			s.add(new AxisPlane(AxisPlane.Axis.Y, 30,d, 10));
			
			s.add(new AxisPlane(AxisPlane.Axis.Y, -30, new SolidColorMaterial(Color.WHITE), 10));
			SolidColorMaterial sm2=new SolidColorMaterial(Color.white);
			sm2.glow=0;
			s.add(new AxisPlane(AxisPlane.Axis.Z, 10,sm2, 30));
			s.add(new AxisPlane(AxisPlane.Axis.X, 10, new SolidColorMaterial(Color.RED), 30));

	
			s.add(new AxisPlane(AxisPlane.Axis.Z, -10, new SolidColorMaterial(Color.white), 30));
			
			s.add(new AxisPlane(AxisPlane.Axis.X, -10, new SolidColorMaterial(Color.GREEN), 30));
			
		} catch (Exception e) {
		}

		return s;		
	}

}
