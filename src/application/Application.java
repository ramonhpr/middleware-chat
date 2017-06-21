package application;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Application extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int width = 800;
	private int height = 480;
	
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
        JTextArea chatArea = new JTextArea();
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
        JTextArea channelArea = new JTextArea();
        channelArea.setLineWrap(true);
        channelArea.setWrapStyleWord(true);
        channelArea.setEditable(false);
        JScrollPane areaScrollChannel = new JScrollPane(channelArea);
        areaScrollChannel.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollChannel.setBorder(
		    BorderFactory.createCompoundBorder(
		        BorderFactory.createCompoundBorder(
		                        BorderFactory.createTitledBorder("Topics"),
		                        BorderFactory.createEmptyBorder(5,5,5,5)),
		areaScrollChannel.getBorder()));
		
		//subscriber
        JTextArea subscriberArea = new JTextArea();
        subscriberArea.setLineWrap(true);
        subscriberArea.setWrapStyleWord(true);
        subscriberArea.setEditable(false);
        JScrollPane areaScrollSubscriber = new JScrollPane(subscriberArea);
        areaScrollSubscriber.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollSubscriber.setBorder(
		    BorderFactory.createCompoundBorder(
		        BorderFactory.createCompoundBorder(
		                        BorderFactory.createTitledBorder("Subscribers"),
		                        BorderFactory.createEmptyBorder(5,5,5,5)),
		areaScrollSubscriber.getBorder()));
        
		//message
        JTextArea messageArea = new JTextArea();
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane areaScrollMessage = new JScrollPane(messageArea);
        areaScrollMessage.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollMessage.setBorder(
		    BorderFactory.createCompoundBorder(
		        BorderFactory.createCompoundBorder(
		                        BorderFactory.createTitledBorder("Message"),
		                        BorderFactory.createEmptyBorder(5,5,5,5)),
		areaScrollMessage.getBorder()));
        
		//send
        JButton send = new JButton("Send");

		//add to panels
		leftPanel.add(areaScrollText);
		rightPanel.add(areaScrollChannel);
		rightPanel.add(areaScrollSubscriber);
		bottomPanel.add(areaScrollMessage, BorderLayout.CENTER);
		bottomPanel.add(send, BorderLayout.EAST);
		
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
		new Application();
	}
}