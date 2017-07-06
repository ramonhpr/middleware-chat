/**
 * 
 */
package distribution;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author avss
 * @author rhpr
 */
public class NamingServer {
	public static final String SERVER_NAME = "server";
	public static final String PUT_NAME = "PUT";
	public static final String GET_ADDRESS = "GET";
	public static final String SEPARATOR = "+";
	
	
	
	private ServerSocket welcomeSocket = null;
	private Socket connectionSocket = null;
	private int receivedMessageSize;
	private DataInputStream inFromClient = null;
	
	
	private static Map<String, InetSocketAddress> map = new HashMap<String, InetSocketAddress>();;
	
	public NamingServer(){
		
	}
	
	public static void main(String[] args) {
		
	}
	
	public static void registerServer(InetSocketAddress server){
		map.put(SERVER_NAME, server);
	}
	
	public static InetSocketAddress getServerAddress(){
		return map.get(SERVER_NAME);
	}
	
	public static void setName(String name, InetSocketAddress value){
		map.put(name, value);
	}
	
	public static InetSocketAddress getAddress(String name){
		return map.get(name);
	}
	
	class ThreadProcessServer implements Runnable {
		public void run() {
			byte[] request = null;
			while (true) {
				try {
					connectionSocket = welcomeSocket.accept();
					System.out.println("Accepting names");
					inFromClient = new DataInputStream(connectionSocket.getInputStream());
					receivedMessageSize = inFromClient.readInt();
					request = new byte[receivedMessageSize];
					inFromClient.read(request, 0, receivedMessageSize);
					inFromClient.close();
					connectionSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		public void handleRequest(byte[] request){
			if (request != null) {
				String str = new String(request);
				String[] strs = str.split(SEPARATOR);
				if(strs[0].equals(PUT_NAME)){
					
					//setName(strs[1], );
				}
			}
		}
	}
}
