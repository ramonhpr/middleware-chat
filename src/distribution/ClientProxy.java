package distribution;

import java.io.Serializable;
import java.util.Random;

public class ClientProxy implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final int BASE_PORT = 1023;
	private static final int MAX_PORT = 65535;
	protected String host;
	protected int port;
	protected int objectId;
	
	public ClientProxy() {
		
	}
	
//	public ClientProxy(final int p) throws UnknownHostExecption {
//		
//	}
	
	
	public int getNewPort(){
		Random random = new Random();
		port = BASE_PORT + random.nextInt(MAX_PORT - BASE_PORT);
		validatePort();
		return port;
	}
	
	private void validatePort(){
		
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
