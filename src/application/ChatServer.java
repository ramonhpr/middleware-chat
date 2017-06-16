package application;

import infrastructure.ServerRequestHandlerReliable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.print.attribute.standard.Severity;

import org.omg.CORBA.ServerRequest;

import distribution.ChatInvoker;
//import distribution.ChatProxy;
import distribution.Marshaller;
import distribution.Message;
import distribution.MessageBody;
import distribution.MessageHeader;

public class ChatServer {
	private static Marshaller marshaller;
	
	public static void main(String[] args) throws IOException, Throwable {
		System.out.println("ChatServer inicializado");
		ServerRequestHandlerReliable srh = new ServerRequestHandlerReliable();
		while(srh.getQueueIN().isEmpty()) {
			System.out.println("loop");
		}
		
		System.out.println("saiu do loop is empty, significa recebeu uma message");
		byte[] received = srh.getQueueIN().remove();
		
    	marshaller = new Marshaller();
    	
    	Message receivedMessage = marshaller.unmarshall(received);
    	System.out.println("recebeu:" + received.toString());
    	
    	MessageHeader messageHeader = new MessageHeader("localhost",1313,"channel 1");
    	MessageBody messageBody = new MessageBody("Bom Dia!");
    	Message message = new Message(messageHeader, messageBody);
    	
		srh.pushOut(marshaller.marshall(message), "localhost", receivedMessage.getHeader().getPort());
		System.out.println("server enviar: Bom Dia");
	}
}
