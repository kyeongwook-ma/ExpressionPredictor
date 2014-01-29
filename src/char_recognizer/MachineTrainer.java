package char_recognizer;


public class MachineTrainer {
	public static void main(String[] args) {
		ExpCharRecognizer.getInstance().preProcess("ex (20)", "");
		ExpCharRecognizer.getInstance().predict();
		System.out.println(ExpCharRecognizer.getInstance().postProcessMLP());
		System.out.println(ExpCharRecognizer.getInstance().postProcessSVM()); 
	}
}
