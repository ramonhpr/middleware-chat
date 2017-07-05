/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distribution;

import infrastructure.ClientRequestHandlerReliable;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import utils.Message;
import utils.MessageBody;
import utils.MessageHeader;

/**
 *
 * @author risa
 */
public class Requestor {
	private int port;
	private String ip;
	private Marshaller marshaller;
	private ClientRequestHandlerReliable crhr;
//	private Callback clientListener;
	private Callback applicationListener;

	public Requestor(final int port, String ip, final Callback applicationCallback) {
		this.port = port;
		this.ip = ip;
		marshaller = new Marshaller();
		applicationListener = applicationCallback;
		crhr = new ClientRequestHandlerReliable(ip, port);
		new Timer().schedule(new Receive(), 0, 1000);
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
	
	class Receive extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(!crhr.isEmpty()){
				System.out.println("crhr receive não é null!");

				byte[] receivedMsg;

				receivedMsg = crhr.receive();
				if (receivedMsg != null) {
					try {
						Message rcvdMsg = marshaller.unmarshall(receivedMsg);
						String msg = rcvdMsg.getBody().getMessage();
						System.out.println(port+" recebeu msg: "+msg);
						applicationListener.onReceive(msg);
					} catch (ClassNotFoundException | IOException
							| InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println("crhr receive é null!");
		}
		
	}
}
