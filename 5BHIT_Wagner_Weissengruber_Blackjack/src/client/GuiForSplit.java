package client;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import misc.Card;
import java.awt.Toolkit;

public class GuiForSplit {

	private JFrame frmBlackjackJavaClient;
	private BlackClient client;
	private Gui mainGui;
	
	//Panels to access every Component later on
	private JPanel playerActionPanel;
	private JPanel playerHandPanel;
	private JLabel topLabel;
	
	//PlayerHand variables
	private ArrayList<JLabel> playerHand;
	private ArrayList<Card> playerCardHand;
	private JLabel playerHandValueLabel;
	private JLabel lblGameStatus;
	
	private int cardCnt = 0;
	private int handValue = 0;
	private int playerBet = 0;
	private GuiForSplit gui2;
	
	//Colours
	Color foregroundColor = new Color(136, 138, 145);
	Color backgroundColor = new Color(48, 49, 54);
	Color specialColor = new Color(55, 57, 63);
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiForSplit window = new GuiForSplit(null, null, null);
					window.frmBlackjackJavaClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GuiForSplit(Gui mainGui, BlackClient client, Card card) 
	{
		this.mainGui = mainGui;
		playerHand = new ArrayList<JLabel>();
		playerCardHand = new ArrayList<Card>();
		this.client = client;
		initialize();
		frmBlackjackJavaClient.setVisible(true);
		
		setCard(card);
		drawCard();
	}

	private void initialize() 
	{
		frmBlackjackJavaClient = new JFrame();
		frmBlackjackJavaClient.setIconImage(Toolkit.getDefaultToolkit().getImage(GuiForSplit.class.getResource("/resources/card_joker_black.png")));
		frmBlackjackJavaClient.setTitle("Blackjack Java Client");
		frmBlackjackJavaClient.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmBlackjackJavaClient.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent event) {
		        OnClose();
		    }
		});
		frmBlackjackJavaClient.getContentPane().setLayout(null);
		frmBlackjackJavaClient.setSize(1000, 700);
		frmBlackjackJavaClient.getContentPane().setBackground(backgroundColor);
		frmBlackjackJavaClient.getContentPane().setForeground(foregroundColor);
		frmBlackjackJavaClient.addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent componentEvent) 
		    {
		        OnResize();
		    }
		});
		
		//Player Action Panel (Middle Panel with Buttons)
		playerActionPanel = new JPanel();
		playerActionPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		playerActionPanel.setBounds(10, 221, 964, 266);
		frmBlackjackJavaClient.getContentPane().add(playerActionPanel);
		playerActionPanel.setLayout(null);
		playerActionPanel.setForeground(foregroundColor);
		playerActionPanel.setBackground(backgroundColor);
		
			//Hit Button
			JButton drawButton = new JButton("Hit");
			drawButton.setFont(new Font("Sitka Small", Font.PLAIN, 30));
			drawButton.setBounds(33, 50, 200, 170);
			drawButton.addActionListener(e -> drawCard());
			drawButton.setName("drawButton");
			drawButton.setForeground(foregroundColor);
			drawButton.setBackground(specialColor);
			playerActionPanel.add(drawButton);
			
			//Stand Button
			JButton standButton = new JButton("Stand");
			standButton.setFont(new Font("Sitka Small", Font.PLAIN, 30));
			standButton.setBounds(731, 50, 200, 170);
			standButton.addActionListener(e -> stand());
			standButton.setName("standButton");
			standButton.setForeground(foregroundColor);
			standButton.setBackground(specialColor);
			playerActionPanel.add(standButton);
			
			//Double Down Button
			JButton doubleDownButton = new JButton("Double");
			doubleDownButton.setFont(new Font("Sitka Small", Font.PLAIN, 30));
			doubleDownButton.setBounds(265, 50, 200, 170);
			doubleDownButton.addActionListener(e -> doubleDown());
			doubleDownButton.setName("doubleDownButton");
			doubleDownButton.setForeground(foregroundColor);
			doubleDownButton.setBackground(specialColor);
			playerActionPanel.add(doubleDownButton);
		
		//Player Hand Panel
		playerHandPanel = new JPanel();
		playerHandPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		playerHandPanel.setLayout(null);
		playerHandPanel.setBounds(10, 115, 964, 96);
		playerHandPanel.setForeground(foregroundColor);
		playerHandPanel.setBackground(backgroundColor);
		frmBlackjackJavaClient.getContentPane().add(playerHandPanel);
			
		playerHandValueLabel = new JLabel("Current player-hand value: ");
		playerHandValueLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		playerHandValueLabel.setBounds(10, 0, 357, 20);
		playerHandValueLabel.setName("playerHandValueLabel");
		playerHandValueLabel.setForeground(foregroundColor);
		playerHandValueLabel.setBackground(backgroundColor);
		playerHandPanel.add(playerHandValueLabel);
			
			//Set Player Hand Images
			int cards = 11;
			for(int i = 0; i < cards; i++)
			{
				JLabel card = new JLabel("");
				card.setIcon(new ImageIcon(GuiForSplit.class.getResource("/resources/card_back.png")));
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
		topLabel.setForeground(foregroundColor);
		frmBlackjackJavaClient.getContentPane().add(topLabel);
		
		lblGameStatus = new JLabel("");
		lblGameStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblGameStatus.setName("gameStatusLabel");
		lblGameStatus.setForeground(new Color(136, 138, 145));
		lblGameStatus.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblGameStatus.setBackground(new Color(48, 49, 54));
		lblGameStatus.setBounds(339, 85, 357, 20);
		frmBlackjackJavaClient.getContentPane().add(lblGameStatus);
	}
	
	protected void OnClose()
	{
		this.mainGui.gui2 = null;
		frmBlackjackJavaClient.setVisible(false);
		frmBlackjackJavaClient.dispose();
	}

	protected void OnResize() 
	{
		if (frmBlackjackJavaClient.getWidth() < 870) 
		{
			frmBlackjackJavaClient.setBounds(frmBlackjackJavaClient.getX(), frmBlackjackJavaClient.getY(), 870, frmBlackjackJavaClient.getHeight());
		}
		
		System.out.println("#GUI# Resizing to fit frame size: width: " + frmBlackjackJavaClient.getWidth() + ", height: " + frmBlackjackJavaClient.getHeight());
		//Resize TopLabel
		topLabel.setBounds(topLabel.getX(), topLabel.getY(), frmBlackjackJavaClient.getWidth() - 20 - 16, topLabel.getHeight());
		
		//Resize PlayerHandPanel
			Component[] handComponents = playerHandPanel.getComponents();
			//Panel Size/Pos
			playerHandPanel.setBounds(playerHandPanel.getX(), playerHandPanel.getY(), frmBlackjackJavaClient.getWidth() - 20 - 16, playerHandPanel.getHeight());
			
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
			playerActionPanel.setBounds(playerActionPanel.getX(), playerActionPanel.getY(), frmBlackjackJavaClient.getWidth() - 20 - 16, playerActionPanel.getHeight());
			
			//			  (Panel Width - Extra Spacing Left&Right - Button Widths) / spacingAmount
			int actionSpacing = (playerActionPanel.getWidth() - 20 - (3 * 200)) / 4;
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
	}

	//Button Actions

	private void doubleDown() 
	{
		System.out.println("#ClientGUI# Request to double down.");
		//Draw last Card
		drawCard();
		
		//double MoneyBet
		playerBet = playerBet * 2;
		
		//Update playerBet from spinner
		frmBlackjackJavaClient.repaint();
		
		//Automaticly Stand & end Round
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stand();
	}

	private void drawCard() 
	{
		System.out.println("#ClientGUI# Request new Card.");
		client.sendMessage("draw", true);
		
		if(cardCnt == 2)
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
		client.sendMessage("stand", true);
		
		Component[] actionComponents = playerActionPanel.getComponents();
		for (Component component : actionComponents) 
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

	//PlayerLogic
	public void setCard(Card card) 
	{
		updateCard(cardCnt, card.getName());
		playerCardHand.add(card);
		handValue += card.getValue();
		cardCnt++;
		
		//Check for duplicate Cards to enable Split
		if (cardCnt == 2 ) 
		{
			Component[] actionComponents = playerActionPanel.getComponents();
			for (Component component : actionComponents) 
			{
				if(component.getName().equals("splitButton"))
				{
					component.setEnabled(false);
					
					Card card1 = playerCardHand.get(0);
					Card card2 = playerCardHand.get(1);
					if (card1.getValue() == card2.getValue()) 
					{
						component.setEnabled(true);
					}
				}
			}
		}
		
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
		frmBlackjackJavaClient.repaint();
	}

	public void updateCard(int pos, String type)
	{
		JLabel card = playerHand.get(pos);
		//Gui	https://www.kenney.nl/assets/playing-cards-pack
		card.setIcon(new ImageIcon(GuiForSplit.class.getResource("/resources/" + type + ".png")));
		frmBlackjackJavaClient.repaint();
	}

	public void endRound(String gameStatus, int dealerHandValue) 
	{
		System.out.println("#Client# Round state: " + gameStatus);
		String gameStatusMSG = "";
		int money = 0;
		switch (gameStatus) 
		{
			case "win":
				money += playerBet * 1.5;
				gameStatusMSG = " You WON " + (int)(playerBet * 1.5) + "€ Dealer has:" + dealerHandValue;
				break;
				
			case "loose":
				money -= playerBet;
				gameStatusMSG = " You LOST " + playerBet + "€ Dealer has:" + dealerHandValue;
				break;
				
			case "draw":
				//Stays the same
				gameStatusMSG = " DRAW  Dealer has:" + dealerHandValue;
				break;
		}
		
		//Update GUI
		lblGameStatus.setText(gameStatusMSG);;
		
		mainGui.addSplitMoney(money);
	}
}
