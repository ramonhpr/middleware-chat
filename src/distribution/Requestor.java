/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distribution;

import infrastructure.ClientRequestHandlerReliable;

import java.io.IOException;

/**
 *
 * @author risa
 */
public class Requestor {
	private int port;
	private String ip;
	private Marshaller marshaller;
	private ClientRequestHandlerReliable crhr;
	private Callback clientListener;

	public Requestor(int port, String ip, Callback listener) {
		this.port = port;
		this.ip = ip;
		marshaller = new Marshaller();
		crhr = new ClientRequestHandlerReliable(ip, port);
		clientListener = listener;
		new Thread(new ReceiveMsgListener()).start();
	}

	public void publishMessage(String msg, String channel) {
		MessageBody body = new MessageBody(msg);
		MessageHeader header = new MessageHeader(ip, port, channel);
		Message message = new Message(header, body);

		try {
			crhr.pushOut(marshaller.marshall(message));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private class ReceiveMsgListener implements Runnable {
		@Override
		public void run() {
			while (true) {
				receive();
			}
		}
		
		private void receive(){
			byte[] receivedMsg = crhr.receive();
			if (receivedMsg != null) {
				returnMessage(receivedMsg);
			}
		}
		
		private void returnMessage(byte[] receivedMsg){
			try {
				Message rcvdMsg = marshaller.unmarshall(receivedMsg);
				clientListener.onReceive(rcvdMsg.getBody().getMessage());
			} catch (ClassNotFoundException | IOException
					| InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
