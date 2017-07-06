/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import distribution.ClientProxy;
import utils.Callback;

/**
 *
 * @author risa
 */
public class ChatClient{
	//private Requestor requestor;
//	private String subscriberName;
	private String subscriberHost;
	private int subscriberPort;
	private String topicName;
	private ClientProxy proxy;
	
	public ChatClient(Callback callback) {
		proxy = new ClientProxy(callback);
	}

	public class Warning extends JFrame implements MouseListener{
		private static final long serialVersionUID = 1L;

		public Warning(String message) {
			JPanel panel = new JPanel();
			panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

			JLabel warningLabel = new JLabel(message);
			warningLabel.setAlignmentX(CENTER_ALIGNMENT);
			panel.add(warningLabel); 

			JButton ok = new JButton("OK");
			ok.setAlignmentX(CENTER_ALIGNMENT);
			ok.addMouseListener(this);
			panel.add(ok);

			add(panel);

			//basic configurations
			setTitle("Warning!");
			setResizable(false);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			pack();
			setLocationRelativeTo(null);
			setVisible(true);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			JButton button = (JButton) e.getSource();
			switch (button.getText()) {
			case "OK":
				dispose();
				break;
			}
		}
	}

	public class NewSubscriber extends JFrame implements MouseListener {
		private static final long serialVersionUID = 1L;
//		private JTextField nameField;
		private JTextField hostField;
		private JTextField portField;

		public NewSubscriber() {
//			nameField = new JTextField("clientName");
			hostField = new JTextField("localhost");
			//portField = new JTextField(String.valueOf(1023+random.nextInt(65535-1023)));
			portField = new JTextField(String.valueOf(proxy.getPort()));
//			JLabel nameLabel = new JLabel("Name: ");
//			nameLabel.setLabelFor(nameField);
			JLabel hostLabel = new JLabel("Host: ");
			hostLabel.setLabelFor(hostField);
			JLabel portLabel = new JLabel("Port: ");
			portLabel.setLabelFor(portField); 

			JPanel textControlsPane = new JPanel();
			GridBagLayout gridbag = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();

			textControlsPane.setLayout(gridbag);

			JLabel[] labels = {/*nameLabel, */hostLabel, portLabel};
			JTextField[] textFields = {/*nameField, */hostField, portField};

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
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			pack();
			setLocationRelativeTo(null);
			setVisible(true);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			JButton button = (JButton) e.getSource();
			switch(button.getText()){
			case "Confirm":
//				String name = nameField.getText();
				String host = hostField.getText();
				String port = portField.getText();
				int portNumber = 0;

				//compara com o padrão valido
//				boolean nameValid = !name.equals("");
				boolean hostValid = host.matches("(\\d{1,3}\\.){3}\\d{1,3}|localhost");
				boolean portValid= port.matches("\\d{4,5}");

//				if(!nameValid){
//					new Warning("Name cannot be empty");
//					System.out.println("Name cannot be empty");
//				}
				if(!hostValid){
					new Warning("Host must be xxx.xxx.xxx.xxx or localhost");
					System.out.println("Host must be xxx.xxx.xxx.xxx or localhost");
				}
				if(portValid){
					portNumber = Integer.parseInt(port);
					//se port for valido, verifica se está dentro do limite
					portValid = portNumber <= 65535 && portNumber >= 1024;
				}

				if(/*nameValid && */hostValid && portValid){
					System.out.println("New Subscriber created: "+host+"\\"+String.valueOf(port));
//					subscriberName = name; 
					subscriberHost = host;
					subscriberPort = portNumber;
					
					createSubscriber();
					new Warning("New Subscriber created: "+host+"\\"+String.valueOf(port));
					dispose();
				} else {
					new Warning("Port number must be >= 1024 and <= 65535");
				}

				break;

			case "Cancel":
				dispose();

				break;
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}
	}

	public class NewTopic extends JFrame implements MouseListener{
		private static final long serialVersionUID = 1L;
		private JTextField topicField;
		
		public NewTopic() {
			topicField = new JTextField("middleware");

			JLabel topicLabel = new JLabel("Topic name: ");
			topicLabel.setLabelFor(topicField);

			JPanel textControlsPane = new JPanel();
			GridBagLayout gridbag = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();

			textControlsPane.setLayout(gridbag);

			JLabel[] labels = {topicLabel};
			JTextField[] textFields = {topicField};

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
							BorderFactory.createTitledBorder("Topic:"),
							BorderFactory.createEmptyBorder(5,5,5,5)));

			setLayout(new BorderLayout());
			add(textControlsPane, BorderLayout.CENTER);
			add(buttons, BorderLayout.SOUTH);

			//basic configurations
			setTitle("New Topic");
			setResizable(false);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			pack();
			setLocationRelativeTo(null);
			setVisible(true);
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			JButton button = (JButton) e.getSource();
			switch(button.getText()){
			case "Confirm":
				String topic = topicField.getText();

				boolean topicValid = !topic.equals("");

				if(!topicValid){
					new Warning("Topic name cannot be empty");
					System.out.println("Topic name cannot be empty");
				}
				else {
					System.out.println("New Topic created: "+topic);
//					topicName = topic;
					createTopic(topic);
					new Warning("New Topic created: "+topic);
					dispose();
				}
				break;

			case "Cancel":
				dispose();
				break;
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {			
		}

		@Override
		public void mouseReleased(MouseEvent e) {			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	}
	
	private void createTopic(String topic) {
		//informa a todos que um novo topico foi criado
//		requestor.publishMessage("Topic "+topicName+" created by "+subscriberName, topicName);
//		topicName = topic;
//		requestor.publishMessage("getTopics", "all");
		proxy.publish("Topic "+topic+" created", topic);
		proxy.publish("getTopics", "all");
//		getSubscribers(topic);
	}
	
	public void createSubscriber() {
		//increve o cliente em um canal global
		proxy.publish("getTopics", "all");
		getSubscribers("all");
//		if(topicName != null){
//			requestor.publishMessage(subscriberName+" enter the Topic ", topicName);
//		}
	}
	
	public boolean getSubscribers(String topic) {
		if(topicName != null && topicName.equals(topic)) {
			return false;
		}
		else {
			topicName = topic;
			proxy.publish("getSubscribers", topicName);
			proxy.publish(subscriberHost+"/"+subscriberPort+" entrou no "+topicName, topicName);
			return true;
		}
	}
	
	public void send(String msg) {
//		msg = subscriberName+": "+msg;
		msg = subscriberHost+"/"+subscriberPort+": "+msg;
		proxy.publish(msg, topicName);
	}
	
	public String getTopicName(){
		return topicName;
	}
}
