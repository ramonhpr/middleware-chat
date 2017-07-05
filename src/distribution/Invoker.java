package distribution;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import utils.Cryptographer;
import utils.Message;
import utils.MessageBody;
import utils.MessageHeader;
import infrastructure.ServerCallback;
import infrastructure.ServerRequestHandlerReliable;

public class Invoker {
	private Marshaller marshaller;
	private ServerRequestHandlerReliable srhr;
	private QueueManager queueManager;

	public Invoker() {
		try {
			srhr = new ServerRequestHandlerReliable(new ServerCallback() {		
				@Override
				public void onReceive(String msg) {
					
				}
				
				@Override
				public void onDisconnect(InetSocketAddress iAddress) {
					queueManager.remove(iAddress);
				}
				
				@Override
				public void onReceive() {
					// TODO Auto-generated method stub
				}
			});
			marshaller = new Marshaller();
			queueManager = new QueueManager();
			new Timer().schedule(new Receive(), 0, 1000);
//			new Thread(new ReceiveMsgListener()).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class Receive extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(!srhr.isEmpty()){

				System.out.println("srhr receive n�o � null!");

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
	//					queueManager.printMapMsg();
						if(channel.equals("all") && message.equals("getTopics")){
	//						sendTopics(host,port);
							broadcastTopics();
						}
						else if(message.equals("getSubscribers")){
	//						sendSubscribers(host,port,channel);
							broadcastSubscribers();
						}
						else {
							broadcast(channel, cpyrcvMsg);
						}
					} catch (ClassNotFoundException | IOException
							| InterruptedException e) {
						e.printStackTrace();
						System.out.println("nao retornou a msg");
					}

					System.out.println("msg processada");
				}
			}
			System.out.println("srhr receive � null!");
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
	
	public void broadcastTopics(){
		for(InetSocketAddress subscriber : queueManager.getSubscribers("all")){
			sendTopics(subscriber.getHostName(), subscriber.getPort());
		}
	}
	
	public void broadcastSubscribers(){
		for(InetSocketAddress subscriber : queueManager.getSubscribers("all")){
			sendSubscribers(subscriber.getHostName(), subscriber.getPort(), queueManager.getSubscriberChannel(subscriber));
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