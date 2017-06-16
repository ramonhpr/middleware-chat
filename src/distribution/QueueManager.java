package distribution;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QueueManager {
	private static Map<String,ArrayList<InetSocketAddress>> map;
	private ArrayList<String> listMsg;
	private static Map<String,ArrayList<String>> mapMsg;
	
	public QueueManager()
	{
		map = new HashMap<>();
	}
	
	
	public static void subscribeOnChannel(String channel, String host, int port)
	{
		//implementar method para verificar se o client está em uma única list
		ArrayList<InetSocketAddress> l = null;
		if(map.get(channel) == null)
		{
			l = new ArrayList<InetSocketAddress>();
			map.put(channel, l);
		} else {
			l = map.get(channel);
		}
		InetSocketAddress i = new InetSocketAddress(host, port);
		l.add(i);
	}
	
	public static void publishOnChannel(String channel, String message)
	{
		//seria melhor lista de Message, pois temos que ter o nome do usuario que digitou a msg
		ArrayList<String> l = null;
		if(mapMsg.get(channel) == null)
		{
			l = new ArrayList<String>();
			mapMsg.put(channel, l);
		} else {
			l = mapMsg.get(channel);
		}
		l.add(message);
	}

}
