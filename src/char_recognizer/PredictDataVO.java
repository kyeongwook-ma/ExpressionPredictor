package char_recognizer;
import org.opencv.core.Mat;
import org.opencv.core.Rect;


public class PredictDataVO {
	protected Mat imgMat;
	protected Rect rect;
	protected int expchar;
	protected float accuracy;
	protected boolean up;
	protected boolean down;
	protected boolean subsdown;
	protected boolean subsup;
	
	public PredictDataVO() {
		imgMat = new Mat();
		rect = new Rect();
		up = false;
		down = false;
		subsdown = false;
		subsup = false;		
	}
}

