package distribution;

import java.io.IOException;

import infrastructure.ServerRequestHandlerReliable;

public class Invoker {
	private Marshaller marshaller;
	private ServerRequestHandlerReliable srhr;
	private Callback serverListener;

	public Invoker(Callback listener) {
		try {
			srhr = new ServerRequestHandlerReliable();
			marshaller = new Marshaller();
			serverListener = listener;
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
		private boolean run = true;
		
		@Override
		public void run() {
			while (run) {
				byte[] receivedMsg = srhr.receive();
				if (receivedMsg != null) {
					run = false;
					try {
						Message rcvdMsg = marshaller.unmarshall(receivedMsg);
						serverListener.onReceive(rcvdMsg);
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