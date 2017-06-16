/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distribution;

import java.io.Serializable;

/**
 *
 * @author rhpr
 */
public class MessageHeader implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String ip;
	private int port;
	private String channel;
	
	public MessageHeader() {
	}
	
	public MessageHeader(String ip, int port, String channel) {
		this.ip = ip;
		this.port = port;
		this.channel = channel;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
}