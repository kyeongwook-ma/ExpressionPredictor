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

			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

			// 소켓으로부터 메세지를 읽어들임
			Message reqMsg = (Message)ois.readObject();

			// 각 타입에 맞는 처리를 받아 response 메세지를 만든다.
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


	/*
	 * Type에 맞는 메세지 처리를 한다
	 */
	/**
	 * @param message
	 * @return
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
