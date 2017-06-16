package distribution;

import java.io.IOException;

import infrastructure.ServerRequestHandlerReliable;

public class Invoker {
	private Marshaller marshaller;
	private ServerRequestHandlerReliable srhr;
	private Callback serverListener;
	private QueueManager queueManager;

	public Invoker() {
		try {
			srhr = new ServerRequestHandlerReliable();
			marshaller = new Marshaller();
			serverListener = new Callback(){
				@Override
				public void onReceive(Message msg) {
					// TODO Auto-generated method stub
					String channel = msg.getHeader().getChannel();
					String host = msg.getHeader().getIp();
					int port = msg.getHeader().getPort();
					String message = msg.getBody().getMessage();
					queueManager.subscribeOnChannel(channel, host, port);
					queueManager.publishOnChannel(channel, message);
				}
			};
			System.out.println("inicia o invoker");
			new Thread(new ReceiveMsgListener()).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("nao criou o invoker");
		}
	}

	public void sendMessage(String ip, int port, String msg, String channel) {
		MessageBody body = new MessageBody(msg);
		MessageHeader header = new MessageHeader(ip, port, channel);
		Message message = new Message(header, body);

		try {
			srhr.pushOut(marshaller.marshall(message), ip, port);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	class ReceiveMsgListener implements Runnable {
		@Override
		public void run() {
			while (true) {
				byte[] receivedMsg = srhr.receive();
				if (receivedMsg != null) {
					try {
						Message rcvdMsg = marshaller.unmarshall(receivedMsg);
//						serverListener.onReceive(rcvdMsg);
						System.out.println("Inoker recebeu: "+rcvdMsg.getBody().getMessage());
						
						
					} catch (ClassNotFoundException | IOException
							| InterruptedException e) {
						e.printStackTrace();
						System.out.println("nao retornou a msg");
					}
				}
				else {
					try {
						Thread.sleep(0);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}