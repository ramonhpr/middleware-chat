package application;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import distribution.Callback;

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
		chat = new ChatClient(new Callback() {
			
			@Override
			public void onReceive(String msg) {
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
				messageArea.setText(messageArea.getText() + "Mensagem não enviada. Servidor indisponível.\n");
			}
		});
		new Application();
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
}
