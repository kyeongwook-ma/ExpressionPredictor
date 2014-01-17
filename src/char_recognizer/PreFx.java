package char_recognizer;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class PreFx {
	private Mat img;
	private Mat binaryimg;
	private Mat labelimg;
	private ArrayList<MatOfPoint> contours;
	private ArrayList<Rect> label;
	private ArrayList<Mat> labelmatlist; 
	private String srcfilename;
	public final int SCALE_WIDTH = 28;
	public final int SCALE_HEIGHT = 28;
	
	public PreFx() {
		img = new Mat();
		binaryimg = new Mat();
		contours = new ArrayList<MatOfPoint>();
		label = new ArrayList<Rect>();
		labelmatlist = new ArrayList<Mat>();
	}
	
	public PreFx(String file) {
		this();
		this.loadImg(file);
		labelimg = new Mat(img.size(), CvType.CV_32F);
		labelimg.setTo(new Scalar(255));
	}
	
	public void loadImg(String file) {
		//Load Source Image from a file
		try {	
			Exception e = new Exception();
			srcfilename = file;
			img = Highgui.imread("Image\\"+srcfilename+".jpg");
			if(img.empty())
				throw e;
		} 
		catch(Exception e) {
			System.err.println("Image load fail.");
		}
	}
	
	public void eliminateNoise() {
		Mat hiMat = new Mat();
		
		/* Image Sharpening. */
		Mat tmp = new Mat();
		Imgproc.GaussianBlur(img, tmp, new Size(5, 5), 5);
		Core.addWeighted(img, 2.1, tmp, -1.0, 0, img);
		
		/* Eliminate noise. */
		Imgproc.dilate(img, img, hiMat);
		Imgproc.erode(img, img, hiMat);
		
	}
	
	public void binaryImg() {
		Mat hiMat = new Mat();
		//Source image convert Gray-Scaled image.
		Mat gray = new Mat(img.size(), CvType.CV_8UC1);
		Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
		
		//Eliminate Noise Gray Scaled Image.
		Imgproc.dilate(gray, gray, hiMat);
		Imgproc.erode(gray, gray, hiMat);
		Imgproc.GaussianBlur(gray, gray, new Size(3, 3), 0);
		
		//Gray-Scaled image convert Binary image(inverse).
		Imgproc.adaptiveThreshold(gray, binaryimg, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 17, 7);
		
		//Eliminate Noise Binary Image.
		Imgproc.erode(binaryimg, binaryimg, hiMat);
		Imgproc.dilate(binaryimg, binaryimg, hiMat);
		Imgproc.dilate(binaryimg, binaryimg, hiMat);

	}
	
	public void findContours() {
		
		/* Find contours from binary image. */
		Mat hiMat = new Mat();
		ArrayList<MatOfPoint> tempcontours = new ArrayList<MatOfPoint>();
		
		/*Find most outer contours.*/
		Imgproc.findContours(binaryimg, contours, hiMat, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		
		/*Find most outer and inner hole contours.*/
		Imgproc.findContours(binaryimg, tempcontours, hiMat, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
		
		/*Draw contours on labeling image.*/
		Imgproc.drawContours(labelimg, tempcontours, -1, new Scalar(0), 2);
		
	}
	
	public void labeling(int height, int width) {
		/*Get bounding rectangles from contours array and add label array.
		and Eliminated label size, height and width.*/
		for(int i = 0; i < contours.size(); i++) {
			Rect rect = Imgproc.boundingRect(contours.get(i));
			if(rect.height >= height || rect.width >= width)
				label.add(rect);
		}
	
	}
	
	public void fixLabeling() {
		
		/* Merge Rect to One Character. */
		int numRect = label.size();
		int[] ok = new int[label.size()];
		ArrayList<Rect> fixList = new ArrayList<Rect>();	
		
		/* First, Merge Width-Crossed and inner Rectangles. */ 
		for(int h = 0; h < 3; h++) {
			for(int i = 0; i < numRect; i++) {
				Rect rect = new Rect(label.get(i).x, label.get(i).y, label.get(i).width, label.get(i).height);
				for(int j = 0; j < numRect; j++) {
					int centerX = rect.x + (rect.width / 2);
					if((i != j) && ((centerX >= label.get(j).x) && (centerX <= (label.get(j).x + label.get(j).width)))) {
						int x = rect.x, y = rect.y, width = rect.width, height = rect.height;
						if(rect.x >= label.get(j).x)
							x = label.get(j).x;
						if(rect.y >= label.get(j).y)
							y = label.get(j).y;
						if((rect.x + rect.width) >= (label.get(j).x + label.get(j).width))
							width = (rect.x + rect.width) - x;
						else
							width = (label.get(j).x + label.get(j).width) - x;
						if((rect.y + rect.height) >= (label.get(j).y + label.get(j).height))
							height = (rect.y + rect.height) - y;
						else
							height = (label.get(j).y + label.get(j).height) - y;
						rect.x = x;
						rect.y = y;
						rect.width = width;
						rect.height = height;
						ok[j] = 1;
					}
				}
				if(ok[i] != 1)	//At least one time activate for merge rect or added fixList.
					fixList.add(rect);
				ok[i] = 1;
			}
			label = fixList;
			
			/* Ready for repeat-First Step(Initialize). */
			numRect = label.size();
			ok = new int[label.size()];
			fixList = new ArrayList<Rect>();
		}
		
		/* Second, Merge Cross Rectangles. */ 
		for(int h = 0; h < 3; h++) {
			for(int i = 0; i < numRect; i++) {
				Rect rect = new Rect(label.get(i).x, label.get(i).y, label.get(i).width, label.get(i).height);
				for(int j = 0; j < numRect; j++) {
					if((i != j) && ((label.get(j).x >= rect.x - 2) && (label.get(j).x <= (rect.x + rect.width + 2)))) {
						if((label.get(j).height*label.get(j).width/4 > rect.height*rect.width)
							|| (label.get(j).height*label.get(j).width < rect.height*rect.width/4)) {
							int x = rect.x, y = rect.y, width = rect.width, height = rect.height;
							if(rect.x >= label.get(j).x)
								x = label.get(j).x;
							if(rect.y >= label.get(j).y)
								y = label.get(j).y;
							if((rect.x + rect.width) >= (label.get(j).x + label.get(j).width))
								width = (rect.x + rect.width) - x;
							else
								width = (label.get(j).x + label.get(j).width) - x;
							if((rect.y + rect.height) >= (label.get(j).y + label.get(j).height))
								height = (rect.y + rect.height) - y;
							else
								height = (label.get(j).y + label.get(j).height) - y;
							rect.x = x;
							rect.y = y;
							rect.width = width;
							rect.height = height;
							ok[j] = 1;
						}
					}
				}
				if(ok[i] != 1)	//At least one time activate for merge rect or added fixList.
					fixList.add(rect);
				ok[i] = 1;
			}
			label = fixList;
			
			//Ready for repeat-First Step(Initialize).
			numRect = label.size();
			ok = new int[label.size()];
			fixList = new ArrayList<Rect>();
		}
		
		
	}
	
	public void sortLabeling() {
		ArrayList<Rect> tmp = new ArrayList<Rect>();
		Rect temp;
		tmp.add(label.get(0));
		
		//Sort Label array in X point-based ascending order.
		for(int i = 1; i < label.size(); i++) {
			temp = new Rect(label.get(i).x, label.get(i).y, label.get(i).width, label.get(i).height);
			for(int j = 0; j < tmp.size(); j++) {
				if(temp.x <= tmp.get(j).x) {
					tmp.add(j, temp);
					break;
				}
				else if(j == tmp.size()-1) {
					tmp.add(temp);
					break;
				}
			}
		}
		label = tmp;
	}
	
	public Mat drawLabel() {
		//Return label drawn image. 
		Mat temp = img.clone();
		
		//Draw Label rectangles on source image.
		for(int i = 0; i < label.size(); i++)
			Core.rectangle(temp, new Point(label.get(i).x, label.get(i).y), 
					new Point(label.get(i).x + label.get(i).width, 
							label.get(i).y + label.get(i).height), new Scalar(0, 0, 255));
		
		return temp;
	}
	
	public void createLabelMatList() {
		//Create array of pixel data in label from labelimg.
		for(int i = 0; i < label.size(); i++) {
			Mat partMat = labelimg.submat(label.get(i).y, label.get(i).y+label.get(i).height
									, label.get(i).x, label.get(i).x+label.get(i).width);
			Imgproc.resize(partMat, partMat, new Size(SCALE_WIDTH, SCALE_HEIGHT));
			labelmatlist.add(partMat);
		}
	}
	
	public ArrayList<PredictDataVO> getPredictData() {
		//Allocate data set at instance of PredictDataVO.
		ArrayList<PredictDataVO> predictData = new ArrayList<PredictDataVO>();
		for(int i = 0; i < label.size(); i++) {
			PredictDataVO pd = new PredictDataVO();
			int w = 0;
			pd.imgMat = new Mat(1, SCALE_WIDTH * SCALE_HEIGHT, CvType.CV_32F);
			for(int j = 0; j < labelmatlist.get(i).height(); j++)
				for(int k = 0; k < labelmatlist.get(i).width(); k++) 
					pd.imgMat.put(0, w++, (float)(labelmatlist.get(i).get(j, k)[0]));
			pd.rect = label.get(i);
			predictData.add(pd);
		}	
		return predictData;
	}
	
	public void writePartImage() {
		for(int i = 0; i < labelmatlist.size(); i++) 
			Highgui.imwrite("Image\\Part\\"+srcfilename+"-"+i+".jpg", labelmatlist.get(i));
	}
	
	public void writeTrainData(ArrayList<PredictDataVO> predictData ,String dstfile, String exp) {
		//Create or append write Machine TrainDB file.
		try {
			FileWriter fw = new FileWriter("Machine\\Traindata\\"+dstfile+"-"+srcfilename+".txt");
			RandomAccessFile raf = new RandomAccessFile("Machine\\TrainDB.txt", "rw");
			StringTokenizer tok = new StringTokenizer(exp, "&~");
			String sampletext = raf.readLine();
			String sampleset = raf.readLine();
			int samplecount = Integer.parseInt(sampletext);
			int samplesetcount = Integer.parseInt(sampleset);
			
			raf.seek(0);
			raf.writeBytes((samplecount+exp.length()-(tok.countTokens()-1))+"\r\n");
			raf.writeBytes((samplesetcount+1)+"\r\n");
			fw.write(exp+"\n");
			raf.seek(raf.length());
			raf.writeBytes((exp.length()-(tok.countTokens()-1))+" "+exp+"\r\n");
			
			for(int i = 0; i < labelmatlist.size(); i++) {
				for(int j = 0; j < SCALE_WIDTH * SCALE_HEIGHT; j++) {
					fw.write(predictData.get(i).imgMat.get(0, j)[0]+" ");
					raf.writeBytes(predictData.get(i).imgMat.get(0, j)[0]+" ");
				}
				fw.write("\n");
				raf.writeBytes("\r\n");
			}
			
			fw.close();
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}

