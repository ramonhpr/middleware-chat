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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.sound.sampled.BooleanControl;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import distribution.Requestor;

/**
 *
 * @author risa
 */
public class ChatClient extends JFrame implements MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Requestor> requestors = new ArrayList<>();
	private JTextField hostField;
	private JTextField portField;
		
	public ChatClient() {
        hostField = new JTextField(15);
        portField = new JTextField(5);
        
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
        
        JPanel buttons = new JPanel();
        JButton confirm = new JButton("Confirm");
        confirm.addMouseListener(this);
        buttons.add(confirm);
        JButton cancel = new JButton("Cancel");
        cancel.addMouseListener(this);
        buttons.add(cancel);
        
        textControlsPane.setBorder(
                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Subscriber:"),
                                BorderFactory.createEmptyBorder(5,5,5,5)));
        
        setLayout(new BorderLayout());
        add(textControlsPane, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
		
		//basic configurations
	    setTitle("New Subscriber");
	    setResizable(false);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    pack();
	    setLocationRelativeTo(null);
	    setVisible(true);
	}
	
    public static void main(String[] args) throws UnknownHostException{
    	new ChatClient();
//    	System.out.println("ChatClient inicializado");
//    	requestor = new Requestor(4000, "localhost");
//    	requestor.publishMessage("4000: Olá!", "Channel 1");
//    	try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	requestor.publishMessage("4000: Olá para o channal 2!", "Channel 2");
//    	try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	Requestor requestor2 = new Requestor(220, "localhost");
//    	requestor2.publishMessage("220: Olá do outro user!", "Channel 2");
//    	try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	requestor2.publishMessage("220: como vai aqui é o user 2!", "Channel 1");
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		JButton button = (JButton) e.getSource();
		switch(button.getText()){
		case "Confirm":
			String host = hostField.getText();
			String port = portField.getText();
			int portNumber = 0;
			
			//compara com o padrão valido
			boolean hostValid = host.matches("(\\d{1,3}\\.){3}\\d{1,3}");
			boolean portValid= port.matches("\\d{1,5}");
			
			if(!hostValid){
				new Warning("Host must be xxx.xxx.xxx.xxx or localhost");
				System.out.println("Host must be xxx.xxx.xxx.xxx or localhost");
			}
			if(portValid){
				portNumber = Integer.parseInt(port);
				//se port for valido, verifica se está dentro do limite
				portValid = portNumber <= 65535;
			}
			else{
				System.out.println("Port number must be <= 65535");
			}
			
			if(hostValid && portValid){
		    	System.out.println("New Subscriber created: "+host+"\\"+String.valueOf(port));
		    	Requestor requestor = new Requestor(portNumber, host);
		    	requestors.add(requestor);
			}
			
			break;
			
		case "Cancel":
			System.exit(0);
			
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public class Warning extends JFrame {
		public Warning(String message) {
			JPanel panel = new JPanel();
			panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			
			JLabel warningLabel = new JLabel(message);
			warningLabel.setAlignmentX(CENTER_ALIGNMENT);
			panel.add(warningLabel); 
	        
	        JButton ok = new JButton("OK");
	        ok.setAlignmentX(CENTER_ALIGNMENT);
	        ok.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					System.exit(0);
				}
			});
	        panel.add(ok);
	        
			add(panel);
			
			//basic configurations
		    setTitle("Warning!");
		    setResizable(false);
		    setDefaultCloseOperation(EXIT_ON_CLOSE);
		    pack();
		    setLocationRelativeTo(null);
		    setVisible(true);
		}
	}
}
