package ray;

import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import render.AxisPlane;
import render.DiffuseTexturedMaterial;
import render.Material;
import render.Ray;
import render.Scene;
import render.SolidColorMaterial;
import render.Sphere;

public class SWRenderer extends JFrame {
	/**
	 * 
	 */

	public static int frame = 0;
	public static final float QUALITY = .1f;
	WASDKeyListener wasd;

	public AvgCol avg[][] = new AvgCol[1][1];

	static class AvgCol {
		int weight = 0;
		Vec3 col;

		public synchronized void add(Vec3 c) {
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

			return col == null ? null : col.toColor((float) 2.2);
		}

	}

	private static final long serialVersionUID = 1L;
	protected static final boolean DISABLE_FILTER = true;
	private static final int CORES = 8;

	public static void main(String[] args) throws IOException {
		new SWRenderer();
	}

	SWRenderer() throws IOException {
		super();
		setSize(500, 500);
		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		wasd = (new WASDKeyListener());
		addKeyListener(wasd);
		addMouseMotionListener(wasd);

		Scene s = new Scene();

		s.add(new Sphere(new Vec3(-3, 17, 3), 1, Color.green));
		s.add(new Sphere(new Vec3(-5, 14, 10), -1, Color.blue));
		s.add(new Sphere(new Vec3(-1, 30, 6), 5, "world.jpg"));
		s.add(new Sphere(new Vec3(-5, 30, -19), 10, "rain.jpg"));
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

		s.optimize();

		// s.objs.add(new AxisPlane(AxisPlane.Axis.Y,100,Color.red));
		for (int i = 0; i < CORES; i++) {
			RenderServer srv = new RenderServer(wasd, this, s);

			Thread d = new Thread(srv);
			d.setDaemon(true);
			d.start();
		}

		getContentPane().add(new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics _g) {
				setRes(this.getWidth(), this.getHeight());

				super.paint(_g);
				float arx = this.getWidth() / (this.getHeight() * 1.f);

//				Vec3 dir =MathUtilities.screenToVec(wasd.mousePos,arx,getWidth(),getHeight());
//				Ray ray=new Ray(wasd.off,dir.yaw(wasd.rot));
//				ray = s.checkIntersectColor(ray);
//				ray.pos=ray.pos.scale(.99f);
//				dir = s.checkIntersectColor(ray).pos;
//				int[] z=MathUtilities.toScreen(dir, arx, getWidth(), getHeight());
//				
//				try {
//				for(int i=0;i<9;i++) {
//					for(int ii=0;ii<9;ii++) {
//						
//						avg[z[0]+i][z[1]+ii]=new AvgCol();
//						avg[z[0]+i][z[1]+ii].add(new Vec3(Color.red));
//					}
//				}
//				}catch(Exception e) {}
				int w = getWidth();
				int h = getHeight();
				int type = BufferedImage.TYPE_3BYTE_BGR;

				BufferedImage image = new BufferedImage(w, h, type);

				for (int i = 0; i < avg.length; i++) {

					for (int ii = 0; ii < avg[i].length; ii++) {
						AvgCol cur = avg[i][ii];

						for (int radius = 0; radius < 99; radius++) {

							if (DISABLE_FILTER)
								break;

							if (cur.get() != null)
								break;
							for (float x = 0; x <= 1.; x += 1. / (radius * radius + 2)) {
								// if (cur.weight>= 3)
								// break;
								if (cur.get() != null)
									break;

								int cx = (int) (Math.cos(x * 2 * Math.PI) * radius + .5);
								int cy = (int) (Math.sin(x * 2 * Math.PI) * radius + .5);

								// if(cur.get()==null)
								int cxi = cx + i;
								int cyii = cy + ii;
								if ((cxi) > 0 && (cxi) < avg.length && cyii > 0 && cyii < avg[cxi].length)
									cur = avg[cx + i][cy + ii];
								// else {
								// if(avg[cx + i][cy + ii].get()!=null)
								// cur.add( avg[cx + i][cy + ii].col);
								// }
							}

						}

						// _g.setColor(cur.get());

						// float size=((this.getWidth()*this.getHeight())/(numpx*1f))+1;

						// _g.fillRect(i, ii, (int)size,(int)size);
						// _g.fillRect(i, ii, 1,1);
			
								if (i < w && ii < h && cur.get() != null) {

									image.setRGB(i, ii, cur.get().getRGB());

								}

					}

				}
				_g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
				// frame++;
				// setTitle(frame+"");

				repaint();
			}
		});

		setVisible(true);

	}

	public synchronized void setPixel(Vec3 r, Ray fin) {
		synchronized (this) {
			int[] x = MathUtilities.toScreen(r, oldw / (oldh * 1.f), oldw, oldh);
			if (x[0] > 0 && x[0] < avg.length && x[1] > 0 && x[1] < avg[0].length) {

				avg[x[0]][x[1]].add(fin.light);
				numpx++;
			}
		}
	}

	int oldw = -1;
	int oldh = -1;
	long numpx = 0;
	int reset = 0;

	public synchronized void setRes(int width, int height) {
		if (width == oldw && height == oldh && !wasd.change) {
			return;
		}

		wasd.change = false;
		oldw = width;
		oldh = height;
		numpx = 0;
		reset = 1;
		avg = new AvgCol[oldw + 1][oldh + 1];

		for (int i = 0; i < avg.length; i++) {
			for (int ii = 0; ii < avg[i].length; ii++) {
				avg[i][ii] = new AvgCol();
			}

		}
	}

	public int[] getRes() {
		reset = 0;
		return new int[] { oldw, oldh, reset };
	}

}
