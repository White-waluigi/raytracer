package scenes;

import java.awt.Color;

import ray.Vec3;
import ray.Vec4;
import render.AxisPlane;
import render.DiffuseTexturedMaterial;
import render.Scene;
import render.SolidColorMaterial;
import render.Sphere;


//Puts a snowman with rainbow teeth on the screen
public class Snowman implements SceneTemplate {

	@Override
	public Scene load() {
		Scene s = new Scene(null);
		try {
			s.add(new Sphere(new Vec3(0, 5, -1.3f), .5f, Color.lightGray));
			s.add(new Sphere(new Vec3(0, 5, -.5), .6f, Color.lightGray));
			s.add(new Sphere(new Vec3(0, 5, .5), .7f, Color.lightGray));
			
			s.add(new Sphere(new Vec3(.15, 4.4, -1.4), .03f, Color.black));
			s.add(new Sphere(new Vec3(-.15, 4.4, -1.4), .03f, Color.black));
			
			for(float x=-.9f;x<1f;x+=.3001) {
				
				s.add(new Sphere(new Vec3(0+x*.2f, 4.5, -1.12+Math.sqrt(1-x*x)*.1f), .03f, Vec3.rainbow(x).toColor()  ));
			}
			
			s.add(new AxisPlane(AxisPlane.Axis.Z, 1, new SolidColorMaterial(Color.white), 30));
			
			s.mainLight=new Vec3(0,0,0);
			
		} catch (Exception e) {
		}
		s.attenuation=new Vec4(2,25,0,100);
		return s;
	}

}
