package ray;

public class Tools {
	public float getAngle(Vec2 a) {
	    float angle = (float) Math.toDegrees(Math.atan2(a.y, a.x));
	    if(angle < 0)
	    	angle += 360;
	    return angle;
	}
}
