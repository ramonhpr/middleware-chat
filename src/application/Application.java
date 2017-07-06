package application;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import distribution.ClientProxy;
import utils.Callback;

public class Application extends JFrame implements MouseListener, FocusListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ChatClient chat;
	private int width = 800;
	private int height = 480;
	private static JTextArea chatArea, messageArea;
	private static JList<String> channelList, subscriberList;
	private static Vector<String> channelVector, subscriberVector;
	private static int channelIndex;
	
	// Constructor to setup GUI components and event handlers
	public Application() {
		setLayout(new BorderLayout(5, 5));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setPreferredSize(new Dimension((int)(width*0.7), (int)(height*0.8)));
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setPreferredSize(new Dimension((int)(width*0.3), (int)(height*0.8)));
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.setPreferredSize(new Dimension((int)(width), (int)(height*0.2)));
		
		//chat
        chatArea = new JTextArea();
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setEditable(false);
        JScrollPane areaScrollText = new JScrollPane(chatArea);
        areaScrollText.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollText.setBorder(
		    BorderFactory.createCompoundBorder(
		        BorderFactory.createCompoundBorder(
		                        BorderFactory.createTitledBorder("Chat"),
		                        BorderFactory.createEmptyBorder(5,5,5,5)),
		areaScrollText.getBorder()));
		
		//channel
        channelVector = new Vector<String>();
        channelList = new JList<String>(channelVector);
//        channelList.addFocusListener(this);
        channelList.addMouseListener(this);
        JScrollPane areaScrollChannel = new JScrollPane(channelList);
        areaScrollChannel.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //new topic button
        JButton newTopic = new JButton("New Topic");
        newTopic.addMouseListener(this);
        
        //create border channels
        JPanel borderChannel = new JPanel(new BorderLayout());
        borderChannel.add(areaScrollChannel, BorderLayout.CENTER);
        borderChannel.add(newTopic, BorderLayout.SOUTH);
        borderChannel.setBorder(
    		    BorderFactory.createCompoundBorder(
    			        BorderFactory.createCompoundBorder(
    			                        BorderFactory.createTitledBorder("Topics"),
    			                        BorderFactory.createEmptyBorder(5,5,5,5)),
    			        				borderChannel.getBorder()));
		
		//subscriber
        subscriberVector = new Vector<String>();
        subscriberList = new JList<String>(subscriberVector);
        subscriberList.setEnabled(false);
        JScrollPane areaScrollSubscriber = new JScrollPane(subscriberList);
        areaScrollSubscriber.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        //new subscriber button
//        JButton newClient = new JButton("New Subscriber");
//        newClient.addMouseListener(this);
        
        //create border subscribers
        JPanel borderSubscriber = new JPanel(new BorderLayout());
        borderSubscriber.add(areaScrollSubscriber, BorderLayout.CENTER);
//        borderSubscriber.add(newClient, BorderLayout.SOUTH);
        borderSubscriber.setBorder(
		    BorderFactory.createCompoundBorder(
		        BorderFactory.createCompoundBorder(
		                        BorderFactory.createTitledBorder("Subscribers"),
		                        BorderFactory.createEmptyBorder(5,5,5,5)),
		        				borderSubscriber.getBorder()));
        
		//message
        messageArea = new JTextArea();
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane areaScrollMessage = new JScrollPane(messageArea);
        areaScrollMessage.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
		//send button
        JButton send = new JButton("Send");
        send.addMouseListener(this);
        
        //create border message
        JPanel borderMessage = new JPanel(new BorderLayout());
        borderMessage.add(areaScrollMessage, BorderLayout.CENTER);
        borderMessage.add(send, BorderLayout.EAST);
        borderMessage.setBorder(
		    BorderFactory.createCompoundBorder(
		        BorderFactory.createCompoundBorder(
		                        BorderFactory.createTitledBorder("Message"),
		                        BorderFactory.createEmptyBorder(5,5,5,5)),
		areaScrollMessage.getBorder()));

		//add to panels
		leftPanel.add(areaScrollText);
		rightPanel.add(borderChannel);
		rightPanel.add(borderSubscriber);
		bottomPanel.add(borderMessage);
		
		//add panels to application
		add(leftPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.EAST);
		add(bottomPanel, BorderLayout.SOUTH);
		
		//basic configurations
	    setTitle("Chat");
	    setResizable(false);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    pack();
	    setLocationRelativeTo(null);
	    setVisible(true);
	}
	
	public static void main(String[] args) {
		Application application = new Application();
		chat = application.new ChatClient(new Callback() {
			
			@Override
			public void onReceive(String msg) {
				// TODO Auto-generated method stub
//				byte[] receivedMsg;

//				receivedMsg = crhr.receive();
//				if (receivedMsg != null) {
//					try {
//						Message rcvdMsg = marshaller.unmarshall(receivedMsg);
//						System.out.println(port+" recebeu msg: "+rcvdMsg.getBody().getMessage());
//					} catch (ClassNotFoundException | IOException
//							| InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//				System.out.println("recebeu: "+msg);
				if(msg.indexOf(':') != -1 && msg.substring(0,msg.indexOf(':')).equals("topics")){
					String string = msg.substring(msg.indexOf(':')+1);
					string = string.replace("[", "");
					string = string.replace("]", "");
					string = string.replace(" ", "");
					String[] topics = string.split(",");
//					channelVector.clear();
					for(String topic : topics) {
						//se for topic novo coloca no vetor
						if(!channelVector.contains(topic)) {
							channelVector.add(topic);
							//se o topic for o mesmo que o user está en~tao guarda o indice para selecionar depois
							if(topic.equals(chat.getTopicName())){
								channelIndex = channelVector.indexOf(topic);
							}
						}
					}
					channelList.setListData(channelVector);
					channelList.setSelectedIndex(channelIndex);
//					chat.getSubscribers(channelVector.firstElement());
				}
				else if(msg.indexOf(':') != -1 && msg.substring(0,msg.indexOf(':')).equals("subscribers")){
						String string = msg.substring(msg.indexOf(':')+1);
						string = string.replace("[", "");
						string = string.replace("]", "");
						string = string.replace(" ", "");
						String[] subscribers = string.split(",");
						subscriberVector.clear();
						for(String subscriber : subscribers) {
							subscriberVector.add(subscriber);
						}
						subscriberList.setListData(subscriberVector);
				}
				else {
					chatArea.setText(chatArea.getText()+msg+"\n");
				}
			}

			@Override
			public void onReceive() {				
			}

			@Override
			public void onTimeOut() {
				chatArea.setText(chatArea.getText() + "Servidor indisponível.\n");
			}
		});
		chat.new NewSubscriber();
//		chat.getSubscribers("all");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource().getClass().getSimpleName().equals("JButton")){
			JButton button = (JButton) e.getSource();
			switch (button.getText()) {
				case "New Subscriber":
					//quando cria novo subscriber o outro é sobrescrito
					chat.new NewSubscriber();
					break;
				case "New Topic":
					chat.new NewTopic();
					chatArea.setText("");
					break;
				case "Send":
					chat.send(messageArea.getText());
					messageArea.setText("");
					break;
			}
		}
		if(e.getSource().getClass().getSimpleName().equals("JList")){
			JList jList = (JList) e.getSource();
			channelIndex = jList.getSelectedIndex();
			String selected = channelVector.get(channelIndex);
			if(chat.getSubscribers(selected)){
				chatArea.setText("");
			}
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

	@Override
	public void focusGained(FocusEvent e) {
	}

	@Override
	public void focusLost(FocusEvent e) {
	}
	

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

				//compara com o padrÃ£o valido
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
					//se port for valido, verifica se estÃ¡ dentro do limite
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
		getSubscribers(topic);
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
}
