/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infrastructure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author risa
 */
public class ClientRequestHandler {
    private String host;
    private int port;
    private int sentMessageSize;
    private int receiveMessageSize;
    
    private Socket clientSocket = null;
    private DataOutputStream outToServer = null;
    private DataInputStream inFromServer = null;
    
    public ClientRequestHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public void send(byte [] msg) throws IOException, InterruptedException {
        clientSocket = new Socket(this.host, this.port);
        outToServer = new DataOutputStream(clientSocket.getOutputStream());
        inFromServer = new DataInputStream(clientSocket.getInputStream());
        
        sentMessageSize = msg.length;
        outToServer.writeInt(sentMessageSize);
        outToServer.write(msg,0,sentMessageSize);
        outToServer.flush();
    }
    
    public byte [] receive() throws IOException, InterruptedException, ClassNotFoundException {
        byte[] msg = null;
        
        receiveMessageSize = inFromServer.readInt();
        msg = new byte[receiveMessageSize];
        inFromServer.read(msg,0,receiveMessageSize);
        
        clientSocket.close();
        outToServer.close();
        inFromServer.close();
        
        return msg;
    }
}
