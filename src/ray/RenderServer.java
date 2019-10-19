package ray;

import java.awt.Color;
import java.util.Random;

import render.Ray;
import render.Scene;

public class RenderServer implements Runnable{

	
	private WASDKeyListener kl;
	private SWRenderer parent;
	private Scene s;
	private Random rand;

	public RenderServer(WASDKeyListener kl, SWRenderer r,Scene s){
		this.kl=kl;
		this.parent=r;
		this.s=s;
		rand = new Random(System.currentTimeMillis()+this.hashCode());
	}
	
	@Override
	public void run() { 

		
		//for (int i = 0; i < h * w * (kl.qual*QUALITY); i++) {
		while(true) {

			int[] res=parent.getRes();

			
			int w=res[0];
			int h=res[1];

			
			float arx = w / (h * 1.f);
			
			
			
			float x=(float) (rand.nextFloat()*w);
			float y=(float) (rand.nextFloat()*h);

			
			Vec3 r = MathUtilities.randVec(arx);
			r=new Vec3(r.x,kl.reverse*r.y,r.z);

			Ray fin = s.checkIntersectColor(new Ray(kl.off, r.yaw(kl.rot).pitch(kl.rotp).roll(kl.rotr)));
			
			//int[] ret=MathUtilities.toScreen(r, arx, w, h);
			
			//avg[ret[0]][ret[1]].add(new Vec3(color));
			parent.setPixel(r,fin);

		}
	}

}
