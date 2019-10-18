package ray;

public class Tools {
	public float getAngle(Vec2 angle) {
	    float a = (float) Math.toDegrees(Math.atan2(angle.y, angle.x));

	    if(a < 0){
	        a += 360;
	    }

	    return a;
	}
}
