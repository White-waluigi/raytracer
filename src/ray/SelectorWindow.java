package ray;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import render.lighting.Fullbright;
import render.lighting.Lighting;
import render.lighting.PathTracer;
import render.lighting.RayTracer;
import scenes.Cornell;
import scenes.RandomSpheres;
import scenes.SceneTemplate;
import scenes.Snowman;
import scenes.SpaceBalls;
import scenes.Testplane;

public class SelectorWindow extends JFrame{

	
	/**
	 * This is used to initialize the Renderer, get Information and select the Scene
	 */
	private static final long serialVersionUID = -8386401883837047547L;
	public static void main(String[] args) throws IOException {
		new SelectorWindow().setVisible(true);
	}
	
	public SelectorWindow() {
		
		final SelectorWindow  dis=this;
        JPanel panel = new JPanel();

        panel.setBounds(61, 11, 81, 140);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.add(panel);

        JLabel c1 ;

        c1 = new JLabel("<html><body>"
        		+ "Feel free to resize the window"
        		+ "<br>w: forward   <br>     s:backwards"
        		+ "<br>a:left  <br>      d:right"
        		+ "<br>q:up      <br>    e:down"
        		+ "<br>y:turn left <br>  x:turn right"
        		+ "<br>c:turn up  <br>   v:turn down"
        		+ "<br>b:rotate   <br>   n:rotate back"
        		+ "<br>z:gamma up  <br>  u:gamma down"
        		+ "<br>any key: reset perpective"
        		+ "<br><br></body></html>");
        panel.add(c1);
        

        
        

        
        Lighting[] petStrings = {new PathTracer(),new RayTracer() ,new Fullbright()};
        //Create the combo box, select item at index 4.
        //Indices start at 0, so 4 specifies the pig.
        JComboBox<Lighting> petList = new JComboBox<>(petStrings);
        petList.setMaximumSize( petList.getPreferredSize() );
      	panel.add(petList);

        JButton c2;
        c2 = new JButton("Spaceballs");
        c2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					dis.dispose();
					new SWRenderer(new SpaceBalls(),(Lighting) petList.getSelectedItem());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
        panel.add(c2);
        
        
        c2 = new JButton("Random Spheres");
        c2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					dis.dispose();
					
					new SWRenderer(new RandomSpheres(),(Lighting) petList.getSelectedItem());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
        panel.add(c2);
        
        
        c2 = new JButton("Snowman");     
        c2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
					try {
						dis.dispose();
						new SWRenderer(new Snowman(),(Lighting) petList.getSelectedItem());
					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				
			}
		});
        panel.add(c2);

        c2 = new JButton("Plane");     
        c2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
					try {
						dis.dispose();
						new SWRenderer(new Testplane(),(Lighting) petList.getSelectedItem());
					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				
			}
		});
        panel.add(c2);
        

        c2 = new JButton("Cornell");     
        c2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
					try {
						dis.dispose();
						new SWRenderer(new Cornell(),(Lighting) petList.getSelectedItem());
					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				
			}
		});
        panel.add(c2);        
        this.setMinimumSize(new Dimension(200,500));
	}
}
