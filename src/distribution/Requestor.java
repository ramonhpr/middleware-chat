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
	private static int TIMEOUT = 3000;
	
	private int port;
	private String ip;
	private Marshaller marshaller;
	private ClientRequestHandlerReliable crhr;
	private Callback clientListener;
	private boolean received;
    private Timer timer;
    private boolean last = true;

	public Requestor(final int port, final String ip, final Callback applicationCallback) {
		this.port = port;
		this.ip = ip;
		marshaller = new Marshaller();
		 this.timer = new Timer();
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
						System.out.println(rcvdMsg.getHeader().getIp() + " " + rcvdMsg.getHeader().getPort());
						received = rcvdMsg.getHeader().getPort() == port || rcvdMsg.getHeader().getPort() == 0 ;
						last = true;
						applicationCallback.onReceive(msg);
					} catch (ClassNotFoundException | IOException
							| InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onReceive(String msg) {
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
		System.out.println(msg + "--" +channel);
		try {
			crhr.pushOut(marshaller.marshall(message));
			timer.schedule(new TimerTask() {
					@Override
					public void run() {
						if(last){
							last = false;
							System.out.println("timertask");
							if(!received){
								clientListener.onTimeOut();
								System.out.println("received is false");
							}
							received = false;
						}
					}
				}, TIMEOUT);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
