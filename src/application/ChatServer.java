package application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import distribution.Invoker;
import distribution.NamingServer;

public class ChatServer {
	
	public static void main(String[] args) throws IOException, Throwable {
		System.out.println("ChatServer inicializado");
		new Invoker();
		InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(), Invoker.SERVER_PORT);
		NamingServer.registerServer(address);
	}
}
