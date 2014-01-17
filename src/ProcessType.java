import com.afk.calmera.network.Message;


public interface ProcessType {
	
	/**
	 * @author se
	 * @version 2014. 1. 17.   
	 * @param msg
	 * @return 
	 * create a message
	 */
	Message process(Message msg);
}
