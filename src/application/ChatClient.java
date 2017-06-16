/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.net.UnknownHostException;

import distribution.Callback;
import distribution.Message;
import distribution.Requestor;

/**
 *
 * @author risa
 */
public class ChatClient {
	private static Requestor requestor;
	
    public static void main(String[] args) throws UnknownHostException{
    	System.out.println("ChatClient inicializado");
    	Callback callback = new Callback() {
			@Override
			public void onReceive(Message msg) {
				// TODO Auto-generated method stub
				System.out.println("Client recebeu: "+msg.getBody().getMessage());
				
			}
		};
    	requestor = new Requestor(4000, "localhost", callback);
    	requestor.publishMessage("Olá!", "Channel 1");
    }
}
