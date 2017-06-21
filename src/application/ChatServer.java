package application;

import java.io.IOException;

import distribution.Invoker;

public class ChatServer {
	private static Invoker invoker;
	
	public static void main(String[] args) throws IOException, Throwable {
		System.out.println("ChatServer inicializado");
		invoker = new Invoker();
	}
}
