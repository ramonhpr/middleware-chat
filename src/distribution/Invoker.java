package distribution;

import infrastructure.ServerRequestHandlerReliable;

import java.io.IOException;
import java.net.InetSocketAddress;

import utils.Message;

public class Invoker {
	public static final int SERVER_PORT = 1313;
	
	private Marshaller marshaller;
	private ServerRequestHandlerReliable srhr;
	private QueueManager queueManager;

	public Invoker() {
		try {
			srhr = new ServerRequestHandlerReliable(new Callback() {		
				@Override
				public void onReceive(String msg) {
					
				}
							
				@Override
				public void onReceive() {
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
							if(channel.equals("all") && message.equals("getTopics")){
								broadcastTopics();
							}
							else if(message.equals("getSubscribers")){
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
					}
				}
				
				@Override
				public void onTimeOut() {
				}
			});
			marshaller = new Marshaller();
			queueManager = new QueueManager();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public boolean send(byte[] msg, InetSocketAddress destination) {
		if(srhr.pushOut(msg, destination.getHostName(), destination.getPort())==false){
			return false;
		}
		return true;
	}
	
	public void broadcast(String channel, byte[] message){
		boolean problem = false;
		InetSocketAddress iAdress = null;
		for(InetSocketAddress subscriber : queueManager.getSubscribers(channel)){
			if(send(message, subscriber)==false){
				problem = true;
				iAdress = subscriber;
				break;
			}
		}
		
		if(problem){
			queueManager.remove(iAdress);
			broadcastSubscribers();
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
			e.printStackTrace();
		}
	}
	
	public void sendSubscribers(String host, int port, String channel) {
		try {
			send(marshaller.marshall(new Message(queueManager.getSubscribersString(channel))),new InetSocketAddress(host,port));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}