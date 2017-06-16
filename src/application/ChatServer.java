package application;

import infrastructure.ServerRequestHandlerReliable;

import java.io.IOException;

//import distribution.ChatProxy;

public class ChatServer {
	public static void main(String[] args) throws IOException, Throwable {
		System.out.println("ChatServer inicializado");
		ServerRequestHandlerReliable srh = new ServerRequestHandlerReliable();
	}
}
