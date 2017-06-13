package application;

import infrastructure.ServerRequestHandlerReliable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.print.attribute.standard.Severity;

import org.omg.CORBA.ServerRequest;

import distribution.ChatInvoker;
//import distribution.ChatProxy;

public class ChatServer {
	public static void main(String[] args) throws IOException, Throwable {
		System.out.println("ChatServer inicializado");
		ServerRequestHandlerReliable srh = new ServerRequestHandlerReliable(3000);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
			objectStream.writeObject("Hello");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
