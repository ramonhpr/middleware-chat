package distribution;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QueueManager {
	private Map<String,ArrayList<InetSocketAddress>> map;
	private ArrayList<String> listMsg;
	private Map<String,ArrayList<String>> mapMsg;
	
	public QueueManager()
	{
		map = new HashMap<>();
	}
	
	
	public void subscribeOnChannel(String channel, String host, int port)
	{
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
	
	public void publishOnChannel(String channel, String message)
	{
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
