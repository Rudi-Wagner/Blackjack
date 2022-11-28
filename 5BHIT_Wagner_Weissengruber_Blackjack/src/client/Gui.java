package client;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingConstants;

import misc.Card;

import javax.swing.JButton;
import javax.swing.JSpinner;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;

public class Gui {

	private JFrame frame;
	private BlackClient client;
	
	//Panels to access every Component later on
	private JPanel playerActionPanel;
	private JPanel playerHandPanel;
	private JPanel playerMoneyPanel;
	private JLabel topLabel;
	
	//PlayerHand variables
	private ArrayList<JLabel> playerHand;
	private JLabel playerHandValueLabel;
	
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
		frame.setSize(1000, 700);
		
		frame.addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent componentEvent) 
		    {
		        OnResize();
		    }
		});
		
		//Player Action Panel (Middle Panel with Buttons)
		playerActionPanel = new JPanel();
		playerActionPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		playerActionPanel.setBounds(10, 283, 964, 266);
		frame.getContentPane().add(playerActionPanel);
		playerActionPanel.setLayout(null);
		
			//Hit Button
			JButton drawButton = new JButton("Hit");
			drawButton.setFont(new Font("Sitka Small", Font.PLAIN, 30));
			drawButton.setBounds(33, 50, 200, 170);
			drawButton.addActionListener(e -> drawCard());
			drawButton.setName("drawButton");
			playerActionPanel.add(drawButton);
			
			//Stand Button
			JButton standButton = new JButton("Stand");
			standButton.setFont(new Font("Sitka Small", Font.PLAIN, 30));
			standButton.setBounds(731, 50, 200, 170);
			standButton.addActionListener(e -> stand());
			standButton.setName("standButton");
			playerActionPanel.add(standButton);
			
			//New Round Button
			JButton reloadButton = new JButton("New Round!");
			reloadButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			reloadButton.setBounds(382, 231, 200, 21);
			reloadButton.addActionListener(e -> newRound());
			reloadButton.setName("reloadButton");
			playerActionPanel.add(reloadButton);
			reloadButton.setEnabled(false);
			
			//Double Down Button
			JButton doubleDownButton = new JButton("Double");
			doubleDownButton.setFont(new Font("Sitka Small", Font.PLAIN, 30));
			doubleDownButton.setBounds(265, 50, 200, 170);
			doubleDownButton.addActionListener(e -> doubleDown());
			doubleDownButton.setName("doubleDownButton");
			playerActionPanel.add(doubleDownButton);
			
			//Split Button
			JButton splitButton = new JButton("Split");
			splitButton.addActionListener(e -> split());
			splitButton.setName("splitButton");
			splitButton.setFont(new Font("Sitka Small", Font.PLAIN, 30));
			splitButton.setBounds(498, 50, 200, 170);
			splitButton.setEnabled(false);
			playerActionPanel.add(splitButton);
		
		playerHandPanel = new JPanel();
		playerHandPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		playerHandPanel.setLayout(null);
		playerHandPanel.setBounds(10, 176, 964, 96);
		frame.getContentPane().add(playerHandPanel);
			
		playerHandValueLabel = new JLabel("Current player-hand value: ");
		playerHandValueLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		playerHandValueLabel.setBounds(10, 0, 357, 20);
		playerHandValueLabel.setName("playerHandValueLabel");
		playerHandPanel.add(playerHandValueLabel);
			
			//Set Player Hand Images
			int cards = 11;
			for(int i = 0; i < cards; i++)
			{
				JLabel card = new JLabel("");
				card.setIcon(new ImageIcon(Gui.class.getResource("/resources/card_back.png")));
				card.setBounds(i * 74, 25, 64, 64);
				card.setName("card" + i);
				playerHand.add(card);
				playerHandPanel.add(card);
			}
		
		//Title
		topLabel = new JLabel("BLACKJACK");
		topLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		topLabel.setHorizontalAlignment(SwingConstants.CENTER);
		topLabel.setBounds(10, 10, 964, 80);
		frame.getContentPane().add(topLabel);
		
		//Player Money Panel
		playerMoneyPanel = new JPanel();
		playerMoneyPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		playerMoneyPanel.setLayout(null);
		playerMoneyPanel.setBounds(10, 101, 964, 64);
		frame.getContentPane().add(playerMoneyPanel);
		
			JLabel lblMoney = new JLabel("Money:");
			lblMoney.setFont(new Font("Tahoma", Font.PLAIN, 24));
			lblMoney.setBounds(10, 11, 78, 42);
			lblMoney.setName("moneyLabel");
			playerMoneyPanel.add(lblMoney);
			
			JLabel moneyLabel = new JLabel("0");
			moneyLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
			moneyLabel.setBounds(98, 11, 282, 42);
			moneyLabel.setName("money");
			playerMoneyPanel.add(moneyLabel);
			
			//Spinner for Player Bet
			JSpinner spinner = new JSpinner();
			spinner.setFont(new Font("Tahoma", Font.PLAIN, 24));
			spinner.setBounds(718, 11, 236, 42);
			spinner.setName("betAmount");
			playerMoneyPanel.add(spinner);
			
			JLabel spinnerLabel = new JLabel("Bet amount:");
			spinnerLabel.setBounds(577, 11, 131, 42);
			spinnerLabel.setName("betAmountLabel");
			playerMoneyPanel.add(spinnerLabel);
			spinnerLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
	}

	protected void OnResize() 
	{
		if (frame.getWidth() < 870) 
		{
			frame.setBounds(frame.getX(), frame.getY(), 870, frame.getHeight());
		}
		
		System.out.println("#GUI# Resizing to fit frame size: width: " + frame.getWidth() + ", height: " + frame.getHeight());
		//Resize TopLabel
		topLabel.setBounds(topLabel.getX(), topLabel.getY(), frame.getWidth() - 20 - 16, topLabel.getHeight());
		
		//Resize PlayerHandPanel
			Component[] handComponents = playerHandPanel.getComponents();
			//Panel Size/Pos
			playerHandPanel.setBounds(playerHandPanel.getX(), playerHandPanel.getY(), frame.getWidth() - 20 - 16, playerHandPanel.getHeight());
			
			//			  (Panel Width - Extra Spacing Left&Right - Button Widths) / spacingAmount
			int handSpacing = (playerHandPanel.getWidth() - 20 - (11 * 64)) / 12;
			int handNextX = 10;
			for (Component component : handComponents) 
			{
				if (!component.getName().equals("playerHandValueLabel")) 
				{
					int x = handNextX + handSpacing;
					int y = component.getY();
					component.setLocation(x, y);
					handNextX = component.getX() + component.getWidth();
				}
			}
		
		//Resize PlayerActionPanel
			Component[] actionComponents = playerActionPanel.getComponents();
			//Panel Size/Pos
			playerActionPanel.setBounds(playerActionPanel.getX(), playerActionPanel.getY(), frame.getWidth() - 20 - 16, playerActionPanel.getHeight());
			
			//			  (Panel Width - Extra Spacing Left&Right - Button Widths) / spacingAmount
			int actionSpacing = (playerActionPanel.getWidth() - 20 - (4 * 200)) / 5;
			int actionNextX = 10;
			for (Component component : actionComponents) 
			{
				if (!component.getName().equals("reloadButton")) 
				{
					int x = actionNextX + actionSpacing;
					int y = component.getY();
					component.setLocation(x, y);
					actionNextX = component.getX() + component.getWidth();
				}
				if (component.getName().equals("reloadButton")) 
				{
					int x = (playerActionPanel.getWidth() - component.getWidth())/2;
					int y = component.getY();
					component.setLocation(x, y);
				}
			}
		
		//Resize PlayerMoneyPanel
			Component[] moneyComponents = playerMoneyPanel.getComponents();
			//Panel Size/Pos
			playerMoneyPanel.setBounds(playerMoneyPanel.getX(), playerMoneyPanel.getY(), frame.getWidth() - 20 - 16, playerMoneyPanel.getHeight());
			
			int savePos = 0;
			for (Component component : moneyComponents) 
			{
				if(component.getName().equals("betAmount"))
				{
					int x = playerMoneyPanel.getWidth() - component.getWidth() - 10;
					int y = component.getY();
					component.setLocation(x, y);
					savePos = component.getX();
				}
				if(component.getName().equals("betAmountLabel"))
				{
					int x = savePos - component.getWidth() - 10;
					int y = component.getY();
					component.setLocation(x, y);
				}
			}
	}

	//Button Actions
	private void split() 
	{
		System.out.println("#ClientGUI# Request to split Hand.");
		// TODO Auto-generated method stub
	}

	private void doubleDown() 
	{
		System.out.println("#ClientGUI# Request to double down.");
		//Draw last Card
		drawCard();
		
		//double MoneyBet
		
		//Automaticly Stand & end Round
		stand();
	}

	private void drawCard() 
	{
		System.out.println("#ClientGUI# Request new Card.");
		client.sendMessage("draw");
		
		if(cardCnt == 0)
		{
			//Disable DoubleDown and Split Button
			Component[] components = playerActionPanel.getComponents();
			for (Component component : components) 
			{
				if(component.getName().equals("doubleDownButton") || component.getName().equals("splitButton"))
				{
					//Set reload Button false
					component.setEnabled(false);
				}
			}
		}
	}
	
	private void stand() 
	{
		System.out.println("#ClientGUI# Request to stand.");
		client.sendMessage("stand");
		
		Component[] components = playerActionPanel.getComponents();
		for (Component component : components) 
		{
			if(component.getClass() == JButton.class && !component.getName().equals("reloadButton"))
			{
				//Set all Buttons to false
				component.setEnabled(false);
			}
			
			if(component.getName().equals("reloadButton"))
			{
				//Set reload Button true
				component.setEnabled(true);
			}
		}
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
		Component[] components = playerActionPanel.getComponents();
		for (Component component : components) 
		{
			if(component.getClass() == JButton.class && !component.getName().equals("reloadButton"))
			{
				//Set all Buttons to true
				component.setEnabled(true);
			}
			
			if(component.getName().equals("reloadButton"))
			{
				//Set reload Button false
				component.setEnabled(false);
			}
		}
		
		//Repaint
		frame.repaint();
	}

	//Player Hand Cards
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
		playerHandValueLabel.setText("Current player-hand value: " + handValue + "");
		frame.repaint();
	}

	public void updateCard(int pos, String type)
	{
		JLabel card = playerHand.get(pos);
		card.setIcon(new ImageIcon(Gui.class.getResource("/resources/" + type + ".png")));
		frame.repaint();
	}
}
