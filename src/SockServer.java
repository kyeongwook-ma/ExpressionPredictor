
public class SockServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SockServerController server = new SockServerController(9679);
		
		server.runServer();
		
		
	}
	
	

}
		