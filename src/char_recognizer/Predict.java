package char_recognizer;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.ml.CvANN_MLP;
import org.opencv.ml.CvSVM;


public class Predict {
	public final int MODEL_MLP = 0;
	public final int MODEL_SVM = 1;
	private CvANN_MLP mlp;
	private CvSVM svm;
	private int currentmodel;
	private float annVer;
	
	public Predict() {
		mlp = new CvANN_MLP();
		svm = new CvSVM();
		currentmodel = 0;
		annVer = 1.f;
	}
	
	public Predict(String file) {
		super();
		this.load(file);
	}
	
	public void load(String file) {
		switch(currentmodel) {
			case MODEL_MLP :
				mlp.load(file);
				break;
			case MODEL_SVM : 
				svm.load(file);
				break;
		}
	}
	
	public int findMaxClassIdOfResponse(Mat response) {
		float max_c = -1.f;
		int max_id = -1;
		for(int i = 0; i < response.cols(); i++) {
			float temp = (float) response.get(0, i)[0]; 
			if(temp > max_c) {
				max_c = temp;
				max_id = i;
			}
		}
		return max_id;
	}
	
	public void predict(PredictDataVO data) {
		switch(currentmodel) {
			case MODEL_MLP :
				Mat response = new Mat(1, ExpChar.NUMBER, CvType.CV_32F);
				mlp.predict(data.imgMat, response);
				data.expchar = this.findMaxClassIdOfResponse(response);
				data.accuracy = (float) response.get(0, data.expchar)[0];
				break;
			case MODEL_SVM :
				data.expchar = (int)svm.predict(data.imgMat);
				data.accuracy = -1;
				break;
		}
	}
	
	public float getAnnVer() {
		return annVer;
	}
	
	public void setAnnVer(float ver) {
		this.annVer = ver;
	}
	
	public void setCurrentModel(int model) {
		currentmodel = model;
	}
	
	public int getCurrentModel() {
		return currentmodel;
	}
}
