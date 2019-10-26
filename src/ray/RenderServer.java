package ray;

import java.awt.Color;
import java.util.Random;

import render.Ray;
import render.Scene;


//Does the actual Path Tracing
public class RenderServer implements Runnable{

	
	private WASDKeyListener kl;
	private SWRenderer parent;
	private Scene s;
	private Random rand;
    
	
	public RenderServer(WASDKeyListener kl, SWRenderer r,Scene s){
		this.kl=kl;
		this.parent=r;
		this.s=s;
		
		//New Seed to avoid double rendering of Ray
		rand = new Random(System.currentTimeMillis()+this.hashCode());
	}
	
	@Override
	public void run() { 

		
		//for (int i = 0; i < h * w * (kl.qual*QUALITY); i++) {
		while(true) {

			//Get current Render Parameters
			int[] res=parent.getRes();

			
			int w=res[0];
			int h=res[1];

			//Get aspect ratio
			float arx = w / (h * 1.f);
			
			
			

			//Get random valid Ray direction
			Vec3 r = MathUtilities.randVec(arx,rand);
			r=new Vec3(r.x,r.y,r.z);

			
			//Send the Ray on its way
			Ray fin = s.checkIntersectColor(new Ray(kl.off, r.pitch(kl.rotp).yaw(kl.rot).roll(kl.rotr)));
			
			//int[] ret=MathUtilities.toScreen(r, arx, w, h);
			
			//avg[ret[0]][ret[1]].add(new Vec3(color));
			parent.setPixel(r,fin);

		}
	}

}
