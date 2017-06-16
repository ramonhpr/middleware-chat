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
			new Thread(new ReceiveMsgListener()).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	private class ReceiveMsgListener implements Runnable {
		@Override
		public void run() {
			while (true) {
				receive();
			}
		}
		
		private void receive(){
			byte[] receivedMsg = srhr.receive();
			if (receivedMsg != null) {
				returnMessage(receivedMsg);
			}
		}
		
		private void returnMessage(byte[] receivedMsg){
			try {
				Message rcvdMsg = marshaller.unmarshall(receivedMsg);
				serverListener.onReceive(rcvdMsg);
			} catch (ClassNotFoundException | IOException
					| InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}