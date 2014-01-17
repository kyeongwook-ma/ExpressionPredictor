import com.afk.calmera.network.Message;


/**
 * @author se
 * define error state
 */
public class ErrorState implements ProcessType {

	@Override
	public Message process(Message msg) {
		msg.getBody().setData("unknown error".getBytes());				
		return msg;
	}

}
