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

import distribution.Callback;
import distribution.Invoker;

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

	private Callback serverCallback;

	public ServerRequestHandlerReliable(Callback Callback) throws IOException {
		queueIN = new ArrayDeque<byte[]>();
		queueOUT = new ArrayDeque<byte[]>();
		port = Invoker.SERVER_PORT;
		welcomeSocket = new ServerSocket(port);
		this.serverCallback = Callback;
		(new Thread(new ThreadProcessServer())).start();
	}

	public boolean pushOut(byte[] msg, String host, int port) {
		// FIXME: Associar IP e Porta na mensagem numa mesma classe
		queueOUT.add(msg);
		if (send(host, port) == false) {
			return false;
		}
		return true;
	}

	private boolean send(String host, int port) {
		byte[] msg = queueOUT.remove();
		sentMessageSize = msg.length;
		try {
			Socket s = new Socket(host, port);
			outToClient = new DataOutputStream(s.getOutputStream());
			outToClient.writeInt(sentMessageSize);
			outToClient.write(msg, 0, sentMessageSize);
			outToClient.flush();
			outToClient.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public byte[] receive() {
		return queueIN.poll();
	}

	class ThreadProcessServer implements Runnable {
		public void run() {
			byte[] request = null;
			while (true) {
				try {
					connectionSocket = welcomeSocket.accept();
					System.out.println("Accept connection");
					inFromClient = new DataInputStream(connectionSocket.getInputStream());
					receivedMessageSize = inFromClient.readInt();
					request = new byte[receivedMessageSize];
					inFromClient.read(request, 0, receivedMessageSize);
					queueIN.add(request);
					inFromClient.close();
					serverCallback.onReceive();
					connectionSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
