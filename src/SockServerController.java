import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class SockServerController {

	private ServerSocket serverSocket;
	private int port;
	private boolean isRunning;


	public SockServerController(int port) {
		this.port = port;
	}

	public void runServer() {
		isRunning = true;

		try {
			
			/* create a server socket */
			serverSocket = new ServerSocket(this.port);
			System.out.println("Server is started...");

			/* 
			 * run until admin stop it 
			 * later implement a stop request 
			 */
			while(isRunning) {

				/* accept a client socket */
				Socket clientSocket = serverSocket.accept();

				System.out.println(clientSocket.getInetAddress() + " is connected");

				/* process a request concurrently */
				ProcessThread processThread = new ProcessThread(clientSocket);
				processThread.start();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public void stopServer() {
		isRunning = false;
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}
