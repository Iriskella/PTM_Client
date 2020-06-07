package command;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Command :connect Ip port  
 * Action : connecting to a remote server placed in ip and listening to port
 */
public class ConnectCommand implements Command {
	//Data
	private static Socket server;
	private static PrintWriter outToServer;
	private static boolean isRunning = false;

	@Override
	public int execute(Interpreter i, int index) {
		String ip = i.code.get(index+1);
		int port = Integer.parseInt(i.code.get(index+2));
		try { if(port < 1000 || port > 9999) throw new Exception(); } catch (Exception e) {}
		try {
			server = new Socket(ip,port);
			outToServer = new PrintWriter(server.getOutputStream());
			isRunning = true;
		}catch (Exception e) {}
		return 2;
	}
	
	/**
	 * Sending string to the server
	 * @param line string to send
	 * @throws Exception 
	 */
	public static void sendToServer(String line) throws Exception {
		if(isRunning) {
			outToServer.println(line);
			outToServer.flush();
		}else throw new Exception();
		
	}
	
	/**
	 * Closing the connection to the server
	 * @throws IOException
	 */
	public static void closeConnection() throws IOException {
		if(isRunning) { 
			outToServer.close();
			server.close();
			isRunning = false;
		}
	}	
}
