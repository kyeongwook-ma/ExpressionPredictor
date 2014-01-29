package char_recognizer;
import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;


public class ExpCharRecognizer {

	private static ExpCharRecognizer mInstance;

	static {
		mInstance = new ExpCharRecognizer();
	}

	private ExpCharRecognizer() { }

	public static ExpCharRecognizer getInstance() {

		if(mInstance == null) {
			return new ExpCharRecognizer();
		}
		return mInstance;
	}
	ArrayList<PredictDataVO> mlpPredictData;
	ArrayList<PredictDataVO> svmPredictData;
	//Create instance of PostFx class.
	PostFx postfx = new PostFx();

	public void preProcess(String fileName, String trainstr) {
		///////////////////////////////
		//0.OpenCV2.4.5 Library load.//
		///////////////////////////////
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		

		//////////////////
		//1.Pre Process.//
		//////////////////

		//Create instance of PreFx class and Load source image file.
		String srcname = new String(fileName);
		PreFx prefx = new PreFx(srcname);

		//Eliminate noise and Sharpening.
		prefx.eliminateNoise();

		//Convert source image to binary image.(img -> binaryimg)
		prefx.binaryImg();

		/*Find contours from binary image and Draw contours on labeling image.*/
		prefx.findContours();

		/*Create labels per a character from contours and Fix some labels and Sort Label array in X point-based ascending order.*/ 
		prefx.labeling(12, 12); //Eliminated label size, height and width.
		prefx.sortLabeling();
		prefx.fixLabeling();

		/*Draw Label rectangles on source image and Save as JPEG file.*/
		Mat labelingImage = prefx.drawLabel();
		Highgui.imwrite("./Image/Label/"+srcname+"-Label.jpg", labelingImage);

		/*Create array of pixel data in label from labelimg and Get Predict Data from labelMatList.*/ 
		prefx.createLabelMatList();
		mlpPredictData = prefx.getPredictData();
		svmPredictData = prefx.getPredictData();

		/*Discriminate available Train data from current Predict Data and Write Train DB file.*/
		StringTokenizer token = new StringTokenizer(trainstr, "&~");
		if(trainstr.length()-(token.countTokens()-1) == mlpPredictData.size()) {
			System.out.println("Ok Train.");
			//prefx.writePartImage();
			prefx.writeTrainData(mlpPredictData, "Traindata", trainstr);
		}
	}


	public void predict() {
		//////////////
		//2.Predict.//
		//////////////

		//Create instance of Predict class.
		Predict pdt = new Predict();

		/*Load MLP model from a XML file
		and Return PredictDataVO instance by MLP predict result.*/
		pdt.setCurrentModel(pdt.MODEL_MLP);
		pdt.load("./Machine/MLP_V3.1.xml");
		for(int i = 0; i < mlpPredictData.size(); i++)
			pdt.predict(mlpPredictData.get(i));

		/*Change Machine learning type
		and Load SVM model from a XML file
		and Return PredictDataVO instance by SVM predict result.*/
		pdt.setCurrentModel(pdt.MODEL_SVM);
		pdt.load("./Machine/SVM_V3.1.xml");
		for(int i = 0; i < svmPredictData.size(); i++)
			pdt.predict(svmPredictData.get(i));

	}


	public String postProcessMLP() {
		String str;
		postfx.setData(mlpPredictData);
		postfx.fixPredictExpression();
		str = postfx.toString();
		return postfx.fixStringExpression(str);
	}
	
	public String postProcessSVM() {
		String str;
		postfx.setData(svmPredictData);
		postfx.fixPredictExpression();
		str = postfx.toString();
		return postfx.fixStringExpression(str);
	}
}
