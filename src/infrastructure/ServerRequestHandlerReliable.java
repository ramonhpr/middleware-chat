/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infrastructure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author risa
 */
public class ServerRequestHandlerReliable {
    private int portNumber;
    private ServerSocket welcomeSocket = null;
    private Socket connectionSocket = null;
    
    private int sentMessageSize;
    private int receivedMessageSize;
    private DataOutputStream outToClient = null;
    private DataInputStream inFromClient = null;
    
    public ServerRequestHandlerReliable(int port) {
        this.portNumber = port;
    }
    
    public byte [] receive() throws IOException, Throwable {
        byte [] rcvMsg = null;
        
        welcomeSocket = new ServerSocket(portNumber);
        connectionSocket = welcomeSocket.accept();
        
        outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        inFromClient = new DataInputStream(connectionSocket.getInputStream());
        
        receivedMessageSize = inFromClient.readInt();
        rcvMsg = new byte[receivedMessageSize];
        
        inFromClient.read(rcvMsg,0,receivedMessageSize);
        
        return rcvMsg;
    }
    
    public void send(byte [] msg) throws IOException, InterruptedException {
        sentMessageSize = msg.length;
        outToClient.writeInt(sentMessageSize);
        outToClient.write(msg);
        outToClient.flush();
        
        connectionSocket.close();
        welcomeSocket.close();
        outToClient.close();
        inFromClient.close();
    }
}
