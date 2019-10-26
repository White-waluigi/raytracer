package scenes;

import java.awt.Color;
import java.util.Random;

import ray.Vec3;
import render.AxisPlane;
import render.DiffuseTexturedMaterial;
import render.Material;
import render.Scene;
import render.SolidColorMaterial;
import render.Sphere;

//Puts a few random Spheres with random properties on a plane
public class RandomSpheres implements SceneTemplate {

	@Override
	public Scene load() {
		Random rand=new Random(System.nanoTime());
		
		String textures[]= {"world.jpg","rain.jpg","carpet.jpg","sun.jpg","zelda.jpg","smile.jpg","smear.bmp","NationalBank.jpg","mine.jpg"};
		
		Scene s = new Scene(null);
		try {
			for(int i=0;i<100;i++) {
				Vec3 v=Vec3.rand(1);
				
				Material c=null;
				boolean glow=false;
				switch((int)(rand.nextFloat()*3)) 
				{
				case 0:
					c=new SolidColorMaterial(Vec3.rainbow((float) rand.nextFloat()).toColor());
					
					
					break;
				case 1:
					glow=true;
				case 2:
					c=new DiffuseTexturedMaterial(textures[(int) (textures.length*rand.nextFloat())]);
					((DiffuseTexturedMaterial)c).glow=glow;
					break;
					
				}				
				
				float x=(float) ((float)  rand.nextFloat()*30.0)-15;
				float y=(float) ((float)  rand.nextFloat()*30.0);
				float z=(float) ((float)  rand.nextFloat()*30.0)-15;
				
				float r=(float) ((float)  rand.nextFloat()*(Math.abs(x)+Math.abs(y)+Math.abs(z))*.1f);
				
				s.add(new Sphere(new Vec3(x,y+30,z), r,c   ));
				
			}
			s.add(new AxisPlane(AxisPlane.Axis.Z, 10, new SolidColorMaterial(Color.red), 60));

		} catch (Exception e) {
		}
		s.mainLight=new Vec3(0,50,0);
		return s;
		
	}

}
