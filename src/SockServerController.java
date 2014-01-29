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
		
		while(isRunning) {

			try {
				serverSocket = new ServerSocket(this.port);
				System.out.println("Server is started...");

				while(isRunning) {

					Socket clientSocket = serverSocket.accept();

					System.out.println(clientSocket.getInetAddress() + " is connected");

					ProcessThread processThread = new ProcessThread(clientSocket);
					processThread.start();

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
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
