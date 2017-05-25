package distribution;

import java.io.IOException;

import infrastructure.ServerRequestHandler;

public class ChatInvoker {
	public void invoke(ClientProxy clientProxy) throws IOException, Throwable {
		ServerRequestHandler srh = new ServerRequestHandler(
			clientProxy.getPort());
		byte[] msgToBeUnmarshalled = null;
		byte[] msgMarshalled = null;
		Message msgUnmarshalled = new Message();
		//ChatImpl rObj = new ChatImpl();
		Marshaller mrsh = new Marshaller();
		//Termination ter = new Termination();
		
		while (true) {
            msgToBeUnmarshalled = srh.receive();
            
            msgUnmarshalled = mrsh.unmarshall(msgToBeUnmarshalled);
            
            // switch com operações possiveis do chat
            
            switch (msgUnmarshalled.getBody().getRequestHeader().getOperation())
            {
                
            }
		}
	}
}
