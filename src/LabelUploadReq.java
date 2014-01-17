import com.afk.calmera.network.Message;
import com.afk.calmera.network.MessageBody;
import com.afk.calmera.network.MessageHeader;


public class LabelUploadReq implements ProcessType {

	@Override
	public Message process(Message msg) {
		
		MessageBody labelBody = new MessageBody();
		MessageHeader respHeader = new MessageHeader();
		
		respHeader.setMsgType(MessageHeader.LABEL_DOWNLOAD_RESP);
		
		byte[] labelImg = ImageUtil.convertFiletoByte(msg.getHeader().getFileName()+"-Label.jpg", "Image\\Label\\").clone();
		labelBody.setData(labelImg);

		return new Message(respHeader, labelBody);
	}

}
