package ray;

import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import render.AxisPlane;
import render.DiffuseTexturedMaterial;
import render.Ray;
import render.Scene;
import render.SolidColorMaterial;
import render.Sphere;

public class SWRenderer extends JFrame {
	/**
	 * 
	 */
	
	public static int frame=0;
	public static final float QUALITY=.1f;
	WASDKeyListener wasd;
	
	public AvgCol avg[][]=null;
	
	static class AvgCol {
		int weight = 0;
		Vec3 col;

		public  synchronized void  add(Vec3 c) {
			if (col == null)
				col = new Vec3(0);

			float r = col.x * weight;
			float g = col.y * weight;
			float b = col.z * weight;

			r += c.x;
			g += c.y;
			b += c.z;

			weight++;

			col = new Vec3(r, g, b).scale(1f / weight);
		}

		public Color get() {

			return col == null ? null : col.toColor();
		}

	}

	private static final long serialVersionUID = 1L;
	protected static final boolean DISABLE_FILTER = false;

	

	public static void main(String[] args) throws IOException {
		new SWRenderer();
	}

	SWRenderer() throws IOException {
		super();
		setSize(500, 500);
		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		wasd=(new WASDKeyListener());
		addKeyListener(wasd);
		addMouseMotionListener(wasd);
		
		Scene s = new Scene();

		s.add(new Sphere(new Vec3(-3, 17, 3), 1, Color.green));
		s.add(new Sphere(new Vec3(-5, 14, 10), -1, Color.blue));
		s.add(new Sphere(new Vec3(-1, 30,6), 5, "world.jpg"));
		s.add(new Sphere(new Vec3(-5, 30, -19), 10, "rain.jpg"));
		s.add(new Sphere(new Vec3(4, 13, -1), 1, Color.lightGray));
		s.add(new Sphere(new Vec3(-9, 110, 5), 1, Color.magenta));

		s.add(new Sphere(new Vec3(0, 10, 5), 1f, "carpet.jpg"));
		s.add(new Sphere(new Vec3(0, 4000, 1000), 1000, "sun.jpg"));
		s.add(new Sphere(new Vec3(0, -10, 0), 11, "smile.jpg"));

		s.add(new AxisPlane(AxisPlane.Axis.Y, 30, new DiffuseTexturedMaterial("zelda.jpg"), 10));
		s.add(new AxisPlane(AxisPlane.Axis.Z, 10, new SolidColorMaterial(Color.red), 30));
		s.add(new AxisPlane(AxisPlane.Axis.X, 10, new SolidColorMaterial(Color.green), 30));

		s.add(new AxisPlane(AxisPlane.Axis.Z, -10, new SolidColorMaterial(Color.blue), 30));
		s.add(new AxisPlane(AxisPlane.Axis.X, -10, new SolidColorMaterial(Color.yellow), 30));
		// s.objs.add(new AxisPlane(AxisPlane.Axis.Y,100,Color.red));

		getContentPane().add(new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics _g) {
				//setRes(this.getWidth(), this.getHeight());
				avg = new AvgCol[this.getWidth() + 1][this.getHeight() + 1];

				for (int i = 0; i < avg.length; i++) {

					for (int ii = 0; ii < avg[i].length; ii++) {
						avg[i][ii] = new AvgCol();
					}

				}

				super.paint(_g);

				float arx = this.getWidth() / (this.getHeight() * 1.f);

				for (int i = 0; i < this.getHeight() * this.getWidth() * (wasd.qual*QUALITY); i++) {
					float x=(float) (Math.random()*getWidth());
					float y=(float) (Math.random()*getHeight());

					
					Vec3 r = MathUtilities.randVec(arx);
					r=new Vec3(r.x,wasd.reverse*r.y,r.z);
		
					Color color = s.checkIntersectColor(new Ray(wasd.off, r.yaw(wasd.rot).pitch(wasd.rotp).roll(wasd.rotr))).light.toColor();
					
					int[] ret=MathUtilities.toScreen(r, arx, getWidth(), getHeight());
					
					avg[ret[0]][ret[1]].add(new Vec3(color));

				}
				Vec3 dir =MathUtilities.screenToVec(wasd.mousePos,arx,getWidth(),getHeight());
				Ray ray=new Ray(wasd.off,dir.yaw(wasd.rot));
				ray = s.checkIntersectColor(ray);
				ray.pos=ray.pos.scale(.99f);
				dir = s.checkIntersectColor(ray).pos;
				int[] z=MathUtilities.toScreen(dir, arx, getWidth(), getHeight());
				
				try {
				for(int i=0;i<9;i++) {
					for(int ii=0;ii<9;ii++) {
						
						avg[z[0]+i][z[1]+ii]=new AvgCol();
						avg[z[0]+i][z[1]+ii].add(new Vec3(Color.red));
					}
				}
				}catch(Exception e) {}

				
				for (int i = 0; i < avg.length; i++) {

					for (int ii = 0; ii < avg[i].length; ii++) {
						AvgCol cur = avg[i][ii];


							
							for (int radius = 0; radius < 99; radius++) {

								if ( DISABLE_FILTER)
									break;

								if (cur.get()!=null)
									break;
								for (float x = 0; x <= 1.; x += 1./(radius * radius + 2)) {
									//if (cur.weight>= 3)
									//	break;
									if (cur.get()!=null)
										break;
									
									int cx = (int) (Math.cos(x * 2 * Math.PI) * radius + .5);
									int cy = (int) (Math.sin(x * 2 * Math.PI) * radius + .5);
									
									//if(cur.get()==null)
									int cxi=cx+i;
									int cyii=cy+ii;
									if((cxi)>0&&(cxi)<avg.length&&cyii>0&&cyii< avg[cxi].length )
										cur = avg[cx + i][cy + ii];
									//else {
									//	if(avg[cx + i][cy + ii].get()!=null)
									//		cur.add( avg[cx + i][cy + ii].col);
									//}
								}

							}
		
							

						_g.setColor(cur.get());

						float size=(this.getWidth()*this.getHeight())/(numpx*1f);
						
						_g.fillRect(i, ii, 1,1);
					}

				}
				frame++;
				setTitle(frame+"");
				repaint();

			}
		});
		
		setVisible(true);

	}
	public synchronized void setPixel(Ray r) {
			int[] x=MathUtilities.toScreen(r.dir, oldw/(oldh*1.f),oldw , oldh);
			if(x[0]<0||x[0]>avg.length||x[1]<0||x[1]>avg[0].length) {
				avg[x[0]][x[1]].add(r.light);
				numpx++;
			}
		
	}
	int oldw=-1;
	int oldh=-1;
	long numpx=0;
	public synchronized void setRes(int width,int height) {
			if(width==oldw&&height==oldh&&!wasd.change) {
				return;
			}
			wasd.change=false;
			oldw=width;
			oldh=height;
			numpx=0;
			avg = new AvgCol[this.getWidth() + 1][this.getHeight() + 1];
			
	}
	public synchronized int[] getRes() {
		return new int[]{oldw,oldh};
	}
	
}
