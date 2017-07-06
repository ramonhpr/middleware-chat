package distribution;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class ClientProxy implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final int BASE_PORT = 1023;
	private static final int MAX_PORT = 65535;
	protected String host;
	protected int port;
	protected int objectId;
	private Requestor requestor;
	private String name;
	
	public ClientProxy(Callback callback) {
		port = getNewPort();
		this.name = String.valueOf(port);
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		try {
			InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(), port);
			NamingServer.setName(name, address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		requestor = new Requestor(port, host, callback);
	}
		
	private int getNewPort(){
		Random random = new Random();
		port = BASE_PORT + random.nextInt(MAX_PORT - BASE_PORT);
		validatePort();
		return port;
	}
	
	private void validatePort(){
		
	}
	
	public void registry(String name){
		
	}
	
	public void publish(String message, String channel){
		requestor.publishMessage(name + ": "+message, channel);
	}
	
	public String getHost() {
		return this.host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getObjectId() {
		return this.objectId;
	}
	
	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}
}
