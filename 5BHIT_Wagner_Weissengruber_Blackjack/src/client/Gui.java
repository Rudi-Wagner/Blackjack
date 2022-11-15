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
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(60, 109, 223, 173);
		btnNewButton.addActionListener(e -> drawCard());
		playerActionPanel.add(btnNewButton);
		
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
		client.sendMessage("draw");
	}

	public void setCard(Card card) 
	{
		updateCard(cardCnt, card.getName());
		handValue += card.getValue();
		cardCnt++;
	}
	
	public void updateCard(int pos, String type)
	{
		JLabel card = playerHand.get(pos);
		card.setIcon(new ImageIcon(Gui.class.getResource("/resources/" + type + ".png")));
		frame.repaint();
	}
}
