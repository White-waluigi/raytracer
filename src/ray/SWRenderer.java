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
import scenes.RandomSpheres;
import scenes.SceneTemplate;
import scenes.Snowman;
import scenes.SpaceBalls;

public class SWRenderer extends JFrame {
	/**
	 * 
	 */

	public static int frame = 0;
	public static final float QUALITY = .1f;
	WASDKeyListener wasd;

	public AvgCol avg[][] = new AvgCol[1][1];

	//Used to average together all Incomming pixels from all the Running threads
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

		public Color get(float gamma) {

			return col == null ? null : col.toColor((float) gamma);
		}

	}

	private static final long serialVersionUID = 1L;
	//Filter isn't used anymore since the pictures are rendered fast enough anyway
	protected static final boolean DISABLE_FILTER = true;
	
	private static final int CORES = Runtime.getRuntime().availableProcessors();



	SWRenderer(SceneTemplate sd) throws IOException {
		super();
		//Set Prefered Size
		setSize(500, 500);
		//Resiszing restarts
		setResizable(true);
	
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		//Listen for key input
		wasd = (new WASDKeyListener());
		addKeyListener(wasd);
		addMouseMotionListener(wasd);

		//Load the Scene
		Scene s = sd.load();
		
		//Generate BSV TREE
		s.optimize();

		setTitle("Cores:"+CORES);
		
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
			
			//NOT THE MAIN RENDERER ONLY OUTPUTS THE PIXEL ON THE SCREEN
			public void paint(Graphics _g) {
				
				//Change Resolution for the Render Servers and check if there was a change
				setRes(this.getWidth(), this.getHeight());

				super.paint(_g);
				
				//Get aspect ratio
				float arx = this.getWidth() / (this.getHeight() * 1.f);

				int w = getWidth();
				int h = getHeight();
				
				
				//Fill the Buffered Image
				int type = BufferedImage.TYPE_3BYTE_BGR;

				BufferedImage image = new BufferedImage(w, h, type);

				//Set each Pixel individually
				for (int i = 0; i < avg.length; i++) {

					for (int ii = 0; ii < avg[i].length; ii++) {
						AvgCol cur = avg[i][ii];

						for (int radius = 0; radius < 99; radius++) {

							//Filter is almost slower than rendering, so its pretty useless now
							if (DISABLE_FILTER)
								break;

							if (cur.get(wasd.gamma) != null)
								break;
							for (float x = 0; x <= 1.; x += 1. / (radius * radius + 2)) {
								
								if (cur.get(wasd.gamma) != null)
									break;

								int cx = (int) (Math.cos(x * 2 * Math.PI) * radius + .5);
								int cy = (int) (Math.sin(x * 2 * Math.PI) * radius + .5);

								int cxi = cx + i;
								int cyii = cy + ii;
								if ((cxi) > 0 && (cxi) < avg.length && cyii > 0 && cyii < avg[cxi].length)
									cur = avg[cx + i][cy + ii];

							}

						}


								if (i < w && ii < h && cur.get(wasd.gamma) != null) {
									//Set Pixel gamma correct using averaged pixels
									image.setRGB(i, ii, cur.get(wasd.gamma).getRGB());

								}

					}
					

				}
				
				//Render Image to Screen
				_g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

				//Repeaint once done
				repaint();
			}
		});

		setVisible(true);

	}

	//Average in one Individual Pixel
	public synchronized void setPixel(Vec3 r, Ray fin) {
		synchronized (this) {
			
			//Convert Ray to Screen coordinates
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

	//Change parameters for Render Servers
	public synchronized void setRes(int width, int height) {
		
		//If no changes, do nothing
		if (width == oldw && height == oldh && !wasd.change) {
			return;
		}
		//If change, update everything and set reset flag to 1
		//Also clear image
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
