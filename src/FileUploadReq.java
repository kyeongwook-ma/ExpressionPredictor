import char_recognizer.ExpCharRecognizer;

import com.afk.calmera.network.Message;
import com.afk.calmera.network.MessageBody;
import com.afk.calmera.network.MessageHeader;


/**
 * @author se
 * img file upload request, send a predict string
 */
public class FileUploadReq implements ProcessType {

	@Override
	public Message process(Message msg) {

		MessageBody respBody = new MessageBody(); 
		MessageHeader respHeader = new MessageHeader();
		String fileName = msg.getHeader().getFileName();
		
		try {
			
			respHeader.setMsgType(MessageHeader.FILE_UPLOAD_RESP);
			
			// save bytes to file
			ImageUtil.saveByteToFile(fileName, msg.getBody().getData());

			// process 
			ExpCharRecognizer.getInstance().preProcess(fileName, "");
			ExpCharRecognizer.getInstance().predict();

			String svm = ExpCharRecognizer.getInstance().postProcessSVM();
			System.out.println("svm : " + svm);

			String mlp = ExpCharRecognizer.getInstance().postProcessMLP();
			System.out.println("MLP : "+ mlp);

			StringBuilder sb = new StringBuilder();
			sb.append(svm + "\n" + mlp);


			if(svm != null && mlp != null) {				
				respBody.setData(sb.toString().getBytes());
			} 

		} catch (Exception e) {
			respBody.setData(e.toString().getBytes());
		}

		return new Message( respHeader, respBody );

	}

}
