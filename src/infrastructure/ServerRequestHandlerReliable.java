/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infrastructure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import distribution.Message;

/**
 * 
 * @author risa
 */
public class ServerRequestHandlerReliable {
	private int portNumber;
	private ServerSocket welcomeSocket = null;
	private Socket connectionSocket = null;

	private int sentMessageSize;
	private int receivedMessageSize;
	private DataOutputStream outToClient = null;
	private DataInputStream inFromClient = null;

	private Queue<byte[]> queueIN;
	private Queue<byte[]> queueOUT;

	public ServerRequestHandlerReliable() throws IOException {
		queueIN = new ArrayDeque<byte[]>();
		queueOUT = new ArrayDeque<byte[]>();
		welcomeSocket = new ServerSocket(1313);
		while (true) {
            connectionSocket = welcomeSocket.accept();
            System.out.println("Accept connection");
    		(new Thread(new ThreadProcessServer(connectionSocket))).start();
    		System.out.println("Send to port 4000");
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    		try {
    			ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
    			objectStream.writeObject("Hello");
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		System.out.println("Queue out");
    		pushOut(byteStream.toByteArray(), "localhost", 4000);
        }
	}

	public void pushOut(byte[] msg, String host, int port) {
		// FIXME: Associar IP e Porta na mensagem numa mesma classe	
		queueOUT.add(msg);
		send(host, port);
	}

	private void send(String host, int port) {
		byte[] msg = queueOUT.remove();
		sentMessageSize = msg.length;
		try {
			System.out.println("Create socket on port "+port);
			Socket s = new Socket(host, port);
			outToClient = new DataOutputStream(s.getOutputStream());
			System.out.println("Socket created");
			System.out.println("Message size="+sentMessageSize);
			outToClient.writeInt(sentMessageSize);
			outToClient.write(msg);
			outToClient.flush();
			System.out.println("Message sent!");
			s.close();
			outToClient.close();
		} catch (IOException e) {
			e.printStackTrace();
			pushOut(msg, host, port);
		}
	}

	class ThreadProcessServer implements Runnable {
		private Socket connectionSocket = null;

		public ThreadProcessServer(Socket connectionSocket) {
			this.connectionSocket = connectionSocket;
		}

		public void run() {
			ObjectInputStream inFromClient = null;
			byte[] request = null;

			try {
				inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
			} catch (IOException e1) {
				return;
			}

			while (true) {
				try {
					System.out.println("Read object from port "+ connectionSocket.getPort());
					int size = inFromClient.readInt();
		        	byte[] message = new byte[size];
                	inFromClient.read(message, 0, size);
                	ByteArrayInputStream byteStream = new ByteArrayInputStream(message);
            		ObjectInputStream objectStream = new ObjectInputStream(byteStream);
            		String s = (String) objectStream.readObject();
					System.out.println("Message recive " + s.toString());
					queueIN.add(request);
					inFromClient.close();
				} catch (Exception e1) {
					return;
				}
				
			}
		}
	}
}
