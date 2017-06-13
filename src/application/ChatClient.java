/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import infrastructure.ClientRequestHandlerReliable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;

/**
 *
 * @author risa
 */
public class ChatClient {
    public static void main(String[] args) throws UnknownHostException{
    	System.out.println("ChatClient inicializado");
    	ClientRequestHandlerReliable crh = new ClientRequestHandlerReliable("localhost", 4000);
    	ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
			objectStream.writeObject("oi");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	crh.pushOut(byteStream.toByteArray());
//    	ByteArrayOutputStream byteStream2 = new ByteArrayOutputStream();
//    	ClientRequestHandlerReliable crh2 = new ClientRequestHandlerReliable("localhost", 4000);
//		try {
//			ObjectOutputStream objectStream = new ObjectOutputStream(byteStream2);
//			objectStream.writeObject("Oi");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	crh2.pushOut(byteStream2.toByteArray());
    	
    }
}
