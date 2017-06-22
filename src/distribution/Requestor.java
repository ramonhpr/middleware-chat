/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distribution;

import infrastructure.ClientRequestHandlerReliable;

import java.io.IOException;

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
	private Callback clientListener;

	public Requestor(final int port, String ip) {
		this.port = port;
		this.ip = ip;
		marshaller = new Marshaller();
		clientListener = new Callback() {
			
			@Override
			public void onReceive() {
				// TODO Auto-generated method stub
				byte[] receivedMsg;

				receivedMsg = crhr.receive();
				if (receivedMsg != null) {
					try {
						Message rcvdMsg = marshaller.unmarshall(receivedMsg);
						System.out.println(port+" recebeu msg: "+rcvdMsg.getBody().getMessage());
					} catch (ClassNotFoundException | IOException
							| InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		crhr = new ClientRequestHandlerReliable(ip, port, clientListener);
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
}
