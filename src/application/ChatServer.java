package application;

import java.io.IOException;

import distribution.Callback;
import distribution.Invoker;
import distribution.Message;

public class ChatServer {
	private static Invoker invoker;
	
	public static void main(String[] args) throws IOException, Throwable {
		System.out.println("ChatServer inicializado");
		Callback callback = new Callback() {
			
			@Override
			public void onReceive(Message msg) {
				// TODO Auto-generated method stub
				String ip = msg.getHeader().getIp();
				int port = msg.getHeader().getPort();
				String channel = msg.getHeader().getChannel();
				System.out.println("Server recebeu: "+msg.getBody().getMessage());
				invoker.sendMessage(ip, port, "Bom Dia!", channel);
			}
			
			@Override
			public void onReceive(String msg) {
				// TODO Auto-generated method stub
				
			}
		};
		invoker = new Invoker(callback);
	}
}
