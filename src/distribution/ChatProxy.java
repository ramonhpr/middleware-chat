/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distribution;

import java.util.ArrayList;

/**
 *
 * @author risa
 */
public class ChatProxy extends ClientProxy implements IChat {
    private static final long serialVersionUID = 1L;

    public ChatProxy() {
    	
    }
    
	@Override
	public void send(String msg) throws Throwable {
		Invocation inv = new Invocation();
		Termination ter = new Termination();
		ArrayList<Object> parameters = new ArrayList<Object>();
		class Local {};
		String methodName = null;
		Requestor requestor = new Requestor();
		
		methodName = Local.class.getEnclosingMethod().getName();
		parameters.add(msg);
		
		inv.setObjectId(this.getObjectId());
		inv.setIpAddress(this.getHost());
		inv.setPortNumber(this.getPort());
		inv.setOperationName(methodName);
		inv.setParameters(parameters);
		
		ter = requestor.invoke(inv);
	}
}
