package scenes;

import java.awt.Color;

import ray.Vec3;
import render.AxisPlane;
import render.Scene;
import render.SolidColorMaterial;


//Used to test Lighting
public class Testplane implements SceneTemplate {

	@Override
	public Scene load() {
		Scene s=new Scene(new Vec3(0,20,0));

		s.add(new AxisPlane(AxisPlane.Axis.Z, 1, new SolidColorMaterial(Color.white), 30));
		

		return s;
	}

}
