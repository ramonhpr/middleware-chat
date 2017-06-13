/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infrastructure;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 *
 * @author risa
 */
public class ClientRequestHandlerReliable {
    private String host;
    private int port;
    
    private Socket clientSocket = null;
    private ObjectOutputStream outToServer = null;
    private ObjectInputStream inFromServer = null;
    
    private Queue<byte[]> queueIN;
    private Queue<byte[]> queueOUT;
    
    public void pushOut(byte[] message) {
    	System.out.println("Send "+ message.toString());
		queueOUT.add(message);
		send();
	}

	public ClientRequestHandlerReliable(String host, int port) {
        this.host = host;
        this.port = port;
        this.queueIN = new ArrayDeque<byte[]>();
        this.queueOUT = new ArrayDeque<byte[]>();
        
        try {
			clientSocket = new Socket("localhost", 1313);
			(new Thread(new ThreadReceive(new ServerSocket(port).accept()))).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        
    }
    
	private void send() {
		while (!queueOUT.isEmpty()) {
			byte[] message = queueOUT.remove();
			
	        try {
		        outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		        System.out.println("Write message on socket " + clientSocket.getLocalPort());
		        outToServer.writeInt(message.length);
				outToServer.write(message,0,message.length);
				System.out.println("Sucessfully send");
    	        outToServer.flush();

    	        outToServer.close();
			} catch (IOException e1) {
//				return;
				pushOut(message);
			}  	
		}
	}
    
    class ThreadReceive implements Runnable {
    	private Socket clientSocket = null;

		public ThreadReceive(Socket connectionSocket) {
			this.clientSocket = connectionSocket;
		}
        public void run() {
            while (true) {
            	System.out.println("Running thread!");
	            try {
	            	System.out.println("Create input stream port ");
            		inFromServer = new ObjectInputStream((clientSocket).getInputStream());
            		System.out.println("Read object from port ");
		        	int size = inFromServer.readInt();
		        	byte[] message =new byte[size];
                	inFromServer.read(message, 0, size);
                	queueIN.add(message);
                	ByteArrayInputStream byteStream = new ByteArrayInputStream(message);
            		ObjectInputStream objectStream = new ObjectInputStream(byteStream);
            		String s = (String) objectStream.readObject();
                	System.out.println("Message recive " + s.toString());

					clientSocket.close();
	                inFromServer.close();
	            } catch (Exception e1) {
	            	e1.printStackTrace();
	                return;
	            }
            }
        }
    }
}
