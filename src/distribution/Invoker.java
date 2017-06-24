package distribution;

import java.io.IOException;
import java.net.InetSocketAddress;

import utils.Cryptographer;
import utils.Message;
import utils.MessageBody;
import utils.MessageHeader;
import infrastructure.ServerRequestHandlerReliable;

public class Invoker {
	private Marshaller marshaller;
	private ServerRequestHandlerReliable srhr;
	private QueueManager queueManager;

	public Invoker() {
		try {
			srhr = new ServerRequestHandlerReliable(new Callback() {
				
				@Override
				public void onReceive(String msg) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onReceive() {
					// TODO Auto-generated method stub
					byte[] receivedMsg = srhr.receive();
					if (receivedMsg != null) {
						try {
							byte[] cpyrcvMsg = (byte[]) receivedMsg.clone();
							Message rcvdMsg = marshaller.unmarshall(receivedMsg);
							String host = rcvdMsg.getHeader().getIp();
							int port = rcvdMsg.getHeader().getPort();
							String channel = rcvdMsg.getHeader().getChannel();
							String message = rcvdMsg.getBody().getMessage();
							queueManager.subscribeOnChannel(channel, host, port);
							queueManager.publishOnChannel(rcvdMsg);
							queueManager.printMap();
							queueManager.printMapMsg();
							if(channel.equals("all") && message.equals("getTopics")){
								sendTopics(host,port);
							}
							else if(message.equals("getSubscribers")){
								sendSubscribers(host,port,channel);
							}
							else {
								broadcast(channel, cpyrcvMsg);
							}
						} catch (ClassNotFoundException | IOException
								| InterruptedException e) {
							e.printStackTrace();
							System.out.println("nao retornou a msg");
						}
					}
				}
			});
			marshaller = new Marshaller();
			queueManager = new QueueManager();
//			new Thread(new ReceiveMsgListener()).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public void sendMessage(String ip, int port, String msg, String channel) {
//		MessageBody body = new MessageBody(msg);
//		MessageHeader header = new MessageHeader(ip, port, channel);
//		Message message = new Message(header, body);
//
//		try {
//			srhr.pushOut(marshaller.marshall(message), ip, port);
//		} catch (IOException | InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
	
	public void send(byte[] msg, InetSocketAddress destination) {
		srhr.pushOut(msg, destination.getHostName(), destination.getPort());
	}
	
	public void broadcast(String channel, byte[] message){
		for(InetSocketAddress subscriber : queueManager.getSubscribers(channel)){
			send(message, subscriber);
		}
	}
	
	public void sendTopics(String host, int port) {
		try {
			send(marshaller.marshall(new Message(queueManager.getTopicsString())),new InetSocketAddress(host,port));
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendSubscribers(String host, int port, String channel) {
		try {
			send(marshaller.marshall(new Message(queueManager.getSubscribersString(channel))),new InetSocketAddress(host,port));
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	class ReceiveMsgListener implements Runnable {
//		@Override
//		public void run() {
//			while (true) {
//				byte[] receivedMsg = srhr.receive();
//				if (receivedMsg != null) {
//					try {
//						byte[] cpyrcvMsg = (byte[]) receivedMsg.clone();
//						Message rcvdMsg = marshaller.unmarshall(receivedMsg);
//						String host = rcvdMsg.getHeader().getIp();
//						int port = rcvdMsg.getHeader().getPort();
//						String channel = rcvdMsg.getHeader().getChannel();
//						String message = rcvdMsg.getBody().getMessage();
//						queueManager.subscribeOnChannel(channel, host, port);
//						queueManager.publishOnChannel(rcvdMsg);
//						queueManager.printMap();
//						queueManager.printMapMsg();
//						if(channel.equals("all") && message.equals("getTopics")){
//							sendTopics(host,port);
//						}
//						else if(message.equals("getSubscribers")){
//							sendSubscribers(host,port,channel);
//						}
//						else {
//							broadcast(channel, cpyrcvMsg);
//						}
//					} catch (ClassNotFoundException | IOException
//							| InterruptedException e) {
//						e.printStackTrace();
//						System.out.println("nao retornou a msg");
//					}
//				}
//				else {
//					try {
//						Thread.sleep(0);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//	}
}