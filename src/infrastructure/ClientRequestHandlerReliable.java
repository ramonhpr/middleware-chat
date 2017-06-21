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

/**
 *
 * @author risa
 */
public class ClientRequestHandlerReliable {
    private String host, serverHost;
    private int port, serverPort;
    
    private Socket clientSocket = null;
    private Socket serverSocket = null;
	private ServerSocket welcomeSocket = null;
    private DataOutputStream outToServer = null;
    private DataInputStream inFromServer = null;
    
    private Queue<byte[]> queueIN;
    private Queue<byte[]> queueOUT;

	public ClientRequestHandlerReliable(String host, int port) {
        this.host = host;
        this.port = port;
        this.serverHost = "localhost";
        this.serverPort = 1313;
        this.queueIN = new ArrayDeque<byte[]>();
        this.queueOUT = new ArrayDeque<byte[]>();
        
        try {
			welcomeSocket = new ServerSocket(port);
			(new Thread(new ThreadReceive())).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void pushOut(byte[] message) {
		queueOUT.add(message);
		send();
	}
    
	private void send() {
		while (!queueOUT.isEmpty()) {
			byte[] message = queueOUT.remove();
	        try {
				clientSocket = new Socket(serverHost, serverPort);
		        outToServer = new DataOutputStream(clientSocket.getOutputStream());
		        System.out.println("size of message: " + message.length);
		        outToServer.writeInt(message.length);
				outToServer.write(message,0,message.length);
    	        outToServer.flush();
    	        outToServer.close();
    	        clientSocket.close();
			} catch (IOException e1) {
				pushOut(message);
			}  	
		}
	}
	
	public byte[] receive(){
		return queueIN.poll();
	}
    
    class ThreadReceive implements Runnable {
        public void run() {
        	byte[] message = null;
            while (true) {
	            try {
	            	serverSocket = welcomeSocket.accept();
            		inFromServer = new DataInputStream(serverSocket.getInputStream());
		        	int size = inFromServer.readInt();
		        	System.out.println("client msg size is:"+size);
		        	message = new byte[size];
                	inFromServer.read(message, 0, size);
                	queueIN.add(message);
                	System.out.println("client recebeu msg");
	                inFromServer.close();
	            } catch (Exception e1) {
	            	return;
	            }
            }
        }
    }
}
