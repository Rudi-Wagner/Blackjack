package client;

import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import misc.Card;

import javax.swing.JButton;

public class Gui {

	private JFrame frame;
	private BlackClient client;
	private ArrayList<JLabel> playerHand;
	private ArrayList<JButton> buttons;
	private JButton reloadButton;
	private JLabel handValueLabel;
	private int cardCnt = 0;
	private int handValue = 0;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui(null);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Gui(BlackClient client) {
		playerHand = new ArrayList<JLabel>();
		buttons = new ArrayList<JButton>();
		this.client = client;
		initialize();
		frame.setVisible(true);
	}

	private void initialize() 
	{
		frame = new JFrame();
		frame.getContentPane().setLayout(null);
		frame.setSize(850, 670);
		
		JPanel playerActionPanel = new JPanel();
		playerActionPanel.setBounds(10, 100, 807, 441);
		frame.getContentPane().add(playerActionPanel);
		playerActionPanel.setLayout(null);
		
		JButton drawButton = new JButton("Hit");
		drawButton.setFont(new Font("Sitka Small", Font.PLAIN, 30));
		drawButton.setBounds(60, 109, 223, 173);
		drawButton.addActionListener(e -> drawCard());
		buttons.add(drawButton);
		playerActionPanel.add(drawButton);
		
		JButton standButton = new JButton("Stand");
		standButton.setFont(new Font("Sitka Small", Font.PLAIN, 30));
		standButton.setBounds(528, 109, 223, 173);
		standButton.addActionListener(e -> stand());
		buttons.add(standButton);
		playerActionPanel.add(standButton);
		
		handValueLabel = new JLabel("Current hand value: ");
		handValueLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		handValueLabel.setBounds(10, 410, 235, 21);
		playerActionPanel.add(handValueLabel);
		
		reloadButton = new JButton("New Round!");
		reloadButton.setBounds(671, 410, 126, 21);
		reloadButton.addActionListener(e -> newRound());
		playerActionPanel.add(reloadButton);
		reloadButton.setEnabled(false);
		
		JPanel playerHandPanel = new JPanel();
		playerHandPanel.setBounds(10, 551, 807, 64);
		frame.getContentPane().add(playerHandPanel);
		playerHandPanel.setLayout(null);
		
		//Set Player Hand Images
		int cards = 11;
		for(int i = 0; i <= cards; i++)
		{
			JLabel card = new JLabel("");
			card.setIcon(new ImageIcon(Gui.class.getResource("/resources/card_back.png")));
			card.setBounds(i * 74, 0, 64, 64);
			playerHand.add(card);
			playerHandPanel.add(card);
		}
		
		JLabel topLabel = new JLabel("BLACKJACK");
		topLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		topLabel.setHorizontalAlignment(SwingConstants.CENTER);
		topLabel.setBounds(10, 10, 807, 80);
		frame.getContentPane().add(topLabel);
	}

	private void drawCard() 
	{
		System.out.println("#ClientGUI# Request new Card.");
		client.sendMessage("draw");
	}
	
	private void stand() 
	{
		System.out.println("#ClientGUI# Request to stand.");
		client.sendMessage("stand");
		
		for (int i = 0; i < buttons.size(); i++) 
		{
			JButton button = buttons.get(i);
			button.setEnabled(false);
		}
		reloadButton.setEnabled(true);
	}
	
	private void newRound() 
	{
		System.out.println("#ClientGUI# Reset on Client.");
		//Reset variables
		cardCnt = 0;
		handValue = 0;
		updateHandValue();
		
		//Reset cards
		for (int i = 0; i < playerHand.size(); i++) 
		{
			updateCard(i, "card_back");
		}
		
		//Reset Buttons
		for (int i = 0; i < buttons.size(); i++) 
		{
			JButton button = buttons.get(i);
			button.setEnabled(true);
		}
		reloadButton.setEnabled(false);
		
		//Repaint
		frame.repaint();
	}

	public void setCard(Card card) 
	{
		updateCard(cardCnt, card.getName());
		handValue += card.getValue();
		cardCnt++;
		
		updateHandValue();
		
		if (handValue >= 21 || cardCnt >= 11) 
		{
			//Automaticly stand and stop the user from hitting again
			stand();
		}
	}
	
	private void updateHandValue() 
	{
		handValueLabel.setText("Current hand value: " + handValue);
	}

	public void updateCard(int pos, String type)
	{
		JLabel card = playerHand.get(pos);
		card.setIcon(new ImageIcon(Gui.class.getResource("/resources/" + type + ".png")));
		frame.repaint();
	}
}
