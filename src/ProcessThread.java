import static com.afk.calmera.network.MessageHeader.FILE_UPLOAD_REQ;
import static com.afk.calmera.network.MessageHeader.LABEL_DOWNLOAD_REQ;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.afk.calmera.network.Message;


public class ProcessThread extends Thread {

	private Socket socket;
	private Message respMsg ;

	public ProcessThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		try {

			/* get I/O from client socket */
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

			/* read message from socket */
			Message reqMsg = (Message)ois.readObject();

			/* create a response message corresponding to request type */
			respMsg = procesReqMsg(reqMsg);
			oos.writeObject(respMsg);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 


	}



	/**
	 * @author se
	 * @version 2014. 1. 17.   
	 * @param message
	 * @return 
	 * create a response message with a request type
	 */
	private Message procesReqMsg(Message message) {

		int msgType = message.getMsgType();
		ProcessType process = null;


		switch(msgType) 
		{
		case FILE_UPLOAD_REQ :

			process = new FileUploadReq();
			break;

		case LABEL_DOWNLOAD_REQ : 

			process = new LabelUploadReq();
			break;

		default :
			process = new ErrorState();
			break;
		}
		
		return process.process(message);
	}



}
