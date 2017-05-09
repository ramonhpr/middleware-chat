/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distribution;

import infrastructure.ClientRequestHandler;

/**
 *
 * @author risa
 */
public class Requestor {    
    public Termination invoke(Invocation inv) throws UnknownHostException, IOException, Throwable {
    	ClientRequestHandler crh = new ClientRequestHandler(
    			inv.getClientProxy().getHost(), 
    			inv.getClientProxy().getPort());
    	Marschaller marschaller = new Marschaller();
    	Termination termination = new Termination();
    	byte [] msgMarschalled;
    	byte [] msgToBeUnmarshalled;
    	Message msgUnmarshalled = new Message();
    	
    	RequestHeader requestHeader = new RequestHeader("",0,true,0,inv.getMethodName());
    	RequestBody requestBody = new RequestBody(inv.getParameters());
    	MessageHeader messageHeader = new MessageHeader("MIOP",0,false,0,0);
    	MessageBody messageBody = new MessageBody(requestHeader,requestBody,null,null);
    	Message msgToBeMarshalled = new Message(messageHeader,messageBody);
    	
    	
    }
}
