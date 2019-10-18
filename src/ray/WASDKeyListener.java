package ray;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

public class WASDKeyListener implements KeyListener, MouseMotionListener {

	Vec3 off = new Vec3(0);
	public Vec2 mousePos = new Vec2(0);
	float rot=0;
	float rotp=0;
	float rotr=0;

	int reverse=1;
	float quali=1;
	float qual=1;
	
	boolean change=false;

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		change=true;
		switch (e.getKeyChar()) {
		case 'w':
			off=off.pp(1);break;
		case 's':
			off=off.mm(1);break;
		case 'd':
			off=off.pp(0);break;
		case 'a':
			off=off.mm(0);break;
		case 'q':
			off=off.pp(2);break;
		case 'e':
			off=off.mm(2);break;
		case ' ':
			reverse=-1*reverse;break;
		case 'y':
			rot-=.1;break;
		case 'x':
			rot+=.1;break;
		case 'p':
			quali=(quali+1)%3;break;
		case 'l':
			quali=3;break;
		case 'o':
			quali=4;break;
		case 'c':
			rotr-=.1;break;
		case 'v':
			rotr+=.1;break;
		case 'b':
			rotp-=.1;break;
		case 'n':
			rotp+=.1;break;
		default:
			off = new Vec3(0);
			rotp=0;
			rot=0;
			rotr=0;

		}
		qual=(float) Math.pow(10, quali-1);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}



	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePos=new Vec2(e.getX(),e.getY()-20f);
		
	}

}
