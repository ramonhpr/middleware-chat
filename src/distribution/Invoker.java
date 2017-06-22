package distribution;

import java.io.IOException;

import utils.Message;
import utils.MessageBody;
import utils.MessageHeader;
import infrastructure.ServerRequestHandlerReliable;

public class Invoker {
	private Marshaller marshaller;
	private ServerRequestHandlerReliable srhr;
//	private QueueManager queueManager;

	public Invoker() {
		try {
			srhr = new ServerRequestHandlerReliable();
			marshaller = new Marshaller();
			new Thread(new ReceiveMsgListener()).start();
		} catch (IOException e) {
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

	class ReceiveMsgListener implements Runnable {
		@Override
		public void run() {
			while (true) {
				byte[] receivedMsg = srhr.receive();
				if (receivedMsg != null) {
					try {
						Message rcvdMsg = marshaller.unmarshall(receivedMsg);
						String host = rcvdMsg.getHeader().getIp();
						int port = rcvdMsg.getHeader().getPort();
						String channel = rcvdMsg.getHeader().getChannel();
						String message = rcvdMsg.getBody().getMessage();
						QueueManager.subscribeOnChannel(channel, host, port);
						QueueManager.publishOnChannel(rcvdMsg);
						QueueManager.printMap();
						QueueManager.printMapMsg();
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
						e.printStackTrace();
					}
				}
			}
		}
	}
}