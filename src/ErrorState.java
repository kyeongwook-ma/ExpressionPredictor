import com.afk.calmera.network.Message;


public class ErrorState implements ProcessType {

	@Override
	public Message process(Message msg) {
		msg.getBody().setData("unknown error".getBytes());				
		return msg;
	}

}
