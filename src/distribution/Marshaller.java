package distribution;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.commons.lang3.ArrayUtils;

public class Marshaller {
	public byte [] marshall(Message msgToBeMarshalled) throws IOException, InterruptedException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
		objectStream.writeObject(msgToBeMarshalled);
		
		byte[] byteArray = byteStream.toByteArray();
		ArrayUtils.reverse(byteArray);
		
		return byteArray;
	}
	
	public Message unmarshall(byte [] msgToBeUnmarshalled) throws IOException, InterruptedException, ClassNotFoundException {
		ArrayUtils.reverse(msgToBeUnmarshalled);
		
		ByteArrayInputStream byteStream = new ByteArrayInputStream(msgToBeUnmarshalled);
		ObjectInputStream objectStream = new ObjectInputStream(byteStream);
		
		return (Message) objectStream.readObject();
	}
}
