package distribution;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class QueueManager {
	private static Map<String,ArrayList<InetSocketAddress>> map = new HashMap<>();
	private static Map<String,ArrayList<Message>> mapMsg = new HashMap<>();
	
	public QueueManager()
	{
		map = new HashMap<>();
	}
	
	
	public static void subscribeOnChannel(String channel, String host, int port)
	{
		ArrayList<InetSocketAddress> l = null;
		//se nao tiver lista de inscritos, cria
		if(map.get(channel) == null)
		{
			l = new ArrayList<InetSocketAddress>();
			map.put(channel, l);
		} else {
			l = map.get(channel);
		}
		InetSocketAddress i = new InetSocketAddress(host, port);
		//se o incrito tiver em uma lista, primeiro tira dessa lista e depois coloca no novo canal
		for (Entry<String, ArrayList<InetSocketAddress>> entry : map.entrySet()) {
			if(entry.getValue().contains(i))
			{
				entry.getValue().remove(i);
			}
		}
		l.add(i);
	}
	
	public static void publishOnChannel(Message message)
	{
		InetSocketAddress i = new InetSocketAddress(message.getHeader().getIp(), message.getHeader().getPort());
		String channel = null;
		//percorre a map procurando o channel que o usuario está incrito
		for (Entry<String, ArrayList<InetSocketAddress>> entry : map.entrySet()) {
			if(entry.getValue().contains(i))
			{
				channel = entry.getKey();
			}
		}
		if(channel != null) {
			//seria melhor lista de Message, pois temos que ter o nome do usuario que digitou a msg
			ArrayList<Message> l = null;
			if(mapMsg.get(channel) == null)
			{
				l = new ArrayList<Message>();
				mapMsg.put(channel, l);
			} else {
				l = mapMsg.get(channel);
			}
			l.add(message);
		}
	}
	
	public static void printMap() {
		System.out.println("\nLista de inscritos:");
		for (Entry<String, ArrayList<InetSocketAddress>> entry : map.entrySet()) {
			System.out.println("	channel: "+entry.getKey());
			for (InetSocketAddress subscriber : entry.getValue()) {
				System.out.println("		subscriber: "+subscriber.toString());
			}
		}
	}
	
	public static void printMapMsg() {
		System.out.println("\nLista de mensagens:");
		for (Entry<String, ArrayList<Message>> entry : mapMsg.entrySet()) {
			System.out.println("	channel: "+entry.getKey());
			for (Message subscriber : entry.getValue()) {
				System.out.println("		subscriber: "+subscriber.toString());
			}
		}
	}
	
//	public static void main(String[] args) {
//		System.out.println("test QueueManager");
//		subscribeOnChannel("ch1", "localhost", 1234);
//		subscribeOnChannel("ch1", "localhost", 1);
//		subscribeOnChannel("ch1", "localhost", 2);
//		subscribeOnChannel("ch1", "localhost", 3);
//		subscribeOnChannel("ch1", "localhost", 4);
//		subscribeOnChannel("ch2", "localhost", 1);
//		subscribeOnChannel("ch3", "localhost", 2);
//		subscribeOnChannel("ch4", "localhost", 3);
//		subscribeOnChannel("ch5", "localhost", 4);
//		printMap();
//		publishOnChannel(new Message(new MessageHeader("localhost",1234),new MessageBody("msg from host1234")));
//		publishOnChannel(new Message(new MessageHeader("localhost",1),new MessageBody("msg from numb1")));
//		publishOnChannel(new Message(new MessageHeader("localhost",2),new MessageBody("msg from numb2")));
//		publishOnChannel(new Message(new MessageHeader("localhost",3),new MessageBody("msg from numb3")));
//		publishOnChannel(new Message(new MessageHeader("localhost",4),new MessageBody("msg from numb4")));
//		printMapMsg();
//		System.out.println("end QueueManager");
//	}

}
