/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infrastructure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;

import utils.Callback;

/**
 * 
 * @author risa
 */
public class ServerRequestHandlerReliable {
	private int port;
	private ServerSocket welcomeSocket = null;
	private Socket connectionSocket = null;

	private int sentMessageSize;
	private int receivedMessageSize;
	private DataOutputStream outToClient = null;
	private DataInputStream inFromClient = null;

	private Queue<byte[]> queueIN;
	private Queue<byte[]> queueOUT;
	
//	private Map<InetSocketAddress, Integer> numErrors;
	
	private Callback serverCallback;

	public ServerRequestHandlerReliable(Callback Callback) throws IOException {
		queueIN = new ArrayDeque<byte[]>();
		queueOUT = new ArrayDeque<byte[]>();
//		numErrors = new HashMap<InetSocketAddress, Integer>();
		port = 1313;
		welcomeSocket = new ServerSocket(port);
		this.serverCallback = Callback;
//			/*FIXME: remove this loop on constructor
//			 * Make a thread just to accept connection and a array of 
//			 * socket/thread to handle them */
		(new Thread(new ThreadProcessServer())).start();
	}
	
	public boolean pushOut(byte[] msg, String host, int port) {
		// FIXME: Associar IP e Porta na mensagem numa mesma classe	
		queueOUT.add(msg);
		if(send(host, port)==false){
			return false;
		}
		return true;
	}

	private boolean send(String host, int port) {
		byte[] msg = queueOUT.remove();
		sentMessageSize = msg.length;
		try {
//			System.out.println("Create socket on port "+port);
			Socket s = new Socket(host, port);
			outToClient = new DataOutputStream(s.getOutputStream());
//			System.out.println("Socket created");
//			System.out.println("Message size="+sentMessageSize);
			outToClient.writeInt(sentMessageSize);
			outToClient.write(msg,0,sentMessageSize);
			outToClient.flush();
			outToClient.close();
//			System.out.println("Message sent!");
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
//			InetSocketAddress iAddress = new InetSocketAddress(host, port);
//			Integer count = numErrors.get(iAddress);
//			if(count == null){
//				count = new Integer(0);
//			}
			//if time maior q x
//			count++;
//			numErrors.put(iAddress, count);
//			if(count >= 3){
//				// tira do canal
//				numErrors.remove(iAddress);
//				return false;
//			} else{
//				//quando user sai a port ainda ta na lista de inscrito, ai da error ConnectException
//				pushOut(msg, host, port);
//			}
		}
		return true;
	}
	
	public byte[] receive(){
		return queueIN.poll();
	}

	class ThreadProcessServer implements Runnable {
		public void run() {
			byte[] request = null;
			while(true) {
				try {
					connectionSocket = welcomeSocket.accept();
		            System.out.println("Accept connection");
		            inFromClient = new DataInputStream(connectionSocket.getInputStream());
	//				while (true) {
//						System.out.println("Read object from port "+ connectionSocket.getPort());
						receivedMessageSize = inFromClient.readInt();
//						System.out.println("size: "+receivedMessageSize);
			        	request = new byte[receivedMessageSize];
	                	inFromClient.read(request, 0, receivedMessageSize);
						queueIN.add(request);
//						System.out.println("queueIn is empty:" + queueIN.isEmpty());
						inFromClient.close();
//						System.out.println("size of queueIn is: "+queueIN.size());
						serverCallback.onReceive();
	//				}
					connectionSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
