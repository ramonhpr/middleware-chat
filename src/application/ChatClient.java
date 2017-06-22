/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import distribution.Callback;
import distribution.Message;
import distribution.Requestor;

/**
 *
 * @author risa
 */
public class ChatClient extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Requestor requestor;
		
	public ChatClient() {
        JTextField hostField = new JTextField(15);
        JTextField portField = new JTextField(5);
        
        JLabel hostLabel = new JLabel("Host: ");
        hostLabel.setLabelFor(hostField);
        JLabel portLabel = new JLabel("Port: ");
        portLabel.setLabelFor(portField); 
        
        JPanel textControlsPane = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
 
        textControlsPane.setLayout(gridbag);
 
        JLabel[] labels = {hostLabel, portLabel};
        JTextField[] textFields = {hostField, portField};
        
        GridBagConstraints c1 = new GridBagConstraints();
        c1.anchor = GridBagConstraints.EAST;        
        int numLabels = labels.length;
        
        for (int i = 0; i < numLabels; i++) {
            c1.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
            c1.fill = GridBagConstraints.NONE;      //reset to default
            c1.weightx = 0.0;                       //reset to default
            textControlsPane.add(labels[i], c1);
 
            c1.gridwidth = GridBagConstraints.REMAINDER;     //end row
            c1.fill = GridBagConstraints.HORIZONTAL;
            c1.weightx = 1.0;
            textControlsPane.add(textFields[i], c1);
        }
 
        c.gridwidth = GridBagConstraints.REMAINDER; //last
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        textControlsPane.setBorder(
                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Bem-vindo ao Chat!"),
                                BorderFactory.createEmptyBorder(5,5,5,5)));
        
        setLayout(new BorderLayout());
        add(textControlsPane, BorderLayout.CENTER);
		
		//basic configurations
	    setTitle("New Client");
	    setResizable(false);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    pack();
	    setLocationRelativeTo(null);
	    setVisible(true);
	}
	
    public static void main(String[] args) throws UnknownHostException{
    	new ChatClient();
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
