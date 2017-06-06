/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infrastructure;

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

	public ServerRequestHandlerReliable(int port) throws IOException {
		this.portNumber = port;
		queueIN = new ArrayDeque<byte[]>();
		welcomeSocket = new ServerSocket(1313);
		while (true) {
            connectionSocket = welcomeSocket.accept();
    		(new Thread(new ThreadProcessServer(connectionSocket))).start();
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
			outToClient = new DataOutputStream(new Socket(host, port).getOutputStream());
			outToClient.writeInt(sentMessageSize);
			outToClient.write(msg);
			outToClient.flush();

			connectionSocket.close();
			welcomeSocket.close();
			outToClient.close();
		} catch (IOException e) {
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
					request = (byte[]) inFromClient.readObject();
					queueIN.add(request);
				} catch (Exception e1) {
					return;
				}
			}
		}
	}
}
