package distribution;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import utils.Cryptographer;
import utils.Message;



public class Marshaller {
	public byte [] marshall(Message msgToBeMarshalled) throws IOException, InterruptedException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
		objectStream.writeObject(msgToBeMarshalled);
		
		byte[] byteArray = byteStream.toByteArray();
		
		Cryptographer.codec(byteArray);
    
		
		return byteArray;
	}
	
	public Message unmarshall(byte [] msgToBeUnmarshalled) throws IOException, InterruptedException, ClassNotFoundException {

		Cryptographer.codec(msgToBeUnmarshalled);
		
		ByteArrayInputStream byteStream = new ByteArrayInputStream(msgToBeUnmarshalled);
		ObjectInputStream objectStream = new ObjectInputStream(byteStream);
		
		return (Message) objectStream.readObject();
	}
}
