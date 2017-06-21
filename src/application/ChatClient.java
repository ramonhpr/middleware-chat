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
    	try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	requestor.publishMessage("Olá para o channal 2!", "Channel 2");
    	try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Requestor requestor2 = new Requestor(220, "localhost", callback);
    	requestor2.publishMessage("Olá do outro user!", "Channel 2");
    	try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	requestor2.publishMessage("como vai aqui é o user 2!", "Channel 1");
    }
}
