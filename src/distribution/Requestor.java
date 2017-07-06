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

import utils.Callback;
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
	
	//timeout
	private boolean received;
	private Timer timer;
	private boolean last = true;	
	private static int TIMEOUT = 3000;

	public Requestor(final int port, String ip, final Callback applicationCallback) {
		this.port = port;
		this.ip = ip;
		marshaller = new Marshaller();
		timer = new Timer();
		clientListener = new Callback() {
			
			@Override
			public void onReceive() {
				// TODO Auto-generated method stub
				byte[] receivedMsg;

				receivedMsg = crhr.receive();
				if (receivedMsg != null) {
					try {
						Message rcvdMsg = marshaller.unmarshall(receivedMsg);
						String msg = rcvdMsg.getBody().getMessage();
						System.out.println(port+" recebeu msg: "+msg);
						applicationCallback.onReceive(msg);
						
						//recebeu msg
						System.out.println("port:"+rcvdMsg.getHeader().getPort());
						received = rcvdMsg.getHeader().getPort() == port || rcvdMsg.getHeader().getPort() == 0 ;
//						last = true;
					} catch (ClassNotFoundException | IOException
							| InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onReceive(String msg) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTimeOut() {
				applicationCallback.onTimeOut();
			}
		};
		crhr = new ClientRequestHandlerReliable(port, clientListener);
	}

	public void publishMessage(String msg, String channel) {
		MessageBody body = new MessageBody(msg);
		MessageHeader header = new MessageHeader(ip, port, channel);
		Message message = new Message(header, body);

		try {
			crhr.pushOut(marshaller.marshall(message));
			if(last){
				last = false;
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						if(!received){
							clientListener.onTimeOut();
							System.out.println("timeout");
						}
						received = false;
						last = true;
					}
				}, TIMEOUT);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
