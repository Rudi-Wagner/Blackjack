package main.java.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
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
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import main.java.misc.Card;

/**
 * This is the main-GUI for the BlackClient.
 * It is has a basic BlackJack-Layout.
 * @author Rudi Wagner
 */
public class Gui {

	private JFrame frmBlackjackJavaClient;
	private BlackClient client;

	//Panels to access every Component later on
	private JPanel playerActionPanel;
	private JPanel playerHandPanel;
	private JPanel playerMoneyPanel;
	private JLabel topLabel;

	//PlayerHand variables
	private ArrayList<JLabel> playerHand;
	public ArrayList<Card> playerCardHand;
	private JLabel playerHandValueLabel;
	private JLabel lblGameStatus;

	private int cardCnt = 0;
	private int handValue = 0;
	private int money = 5000;
	private int playerBet = 0;
	public GuiForSplit gui2 = null;

	//Colours
	Color foregroundColor = new Color(136, 138, 145);
	Color backgroundColor = new Color(48, 49, 54);
	Color specialColor = new Color(55, 57, 63);

	/**
	The main method is the entry point of the Java program.
	It invokes the EventQueue's invokeLater method and passes a new instance of the Runnable interface.
	The run method of the Runnable interface is then executed, which creates a new instance of the Gui class
	and sets the visibility of the window to true.
	If any exception occurs, it is caught and the stack trace is printed.
	@param args command line arguments
	*/
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Gui window = new Gui(null);
					window.frmBlackjackJavaClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	Constructor for the Gui class.
	Initializes an ArrayList of JLabels for playerHand and another ArrayList of Cards for playerCardHand.
	Assigns the client parameter to the class field "client".
	Calls the initialize method to set up the GUI.
	Makes the frame visible.
	@param client instance of the BlackClient class
	*/
	public Gui(BlackClient client) {
		playerHand = new ArrayList<>();
		playerCardHand = new ArrayList<>();
		this.client = client;
		initialize();
		frmBlackjackJavaClient.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmBlackjackJavaClient = new JFrame();
		frmBlackjackJavaClient.setIconImage(Toolkit.getDefaultToolkit().getImage(Gui.class.getResource("/resources/card_joker_black.png")));
		frmBlackjackJavaClient.setTitle("Blackjack Java Client");
		frmBlackjackJavaClient.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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
		    @Override
			public void componentResized(ComponentEvent componentEvent)
		    {
		        OnResize();
		    }
		});

		//Player Action Panel (Middle Panel with Buttons)
		playerActionPanel = new JPanel();
		playerActionPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		playerActionPanel.setBounds(10, 283, 964, 266);
		frmBlackjackJavaClient.getContentPane().add(playerActionPanel);
		playerActionPanel.setLayout(null);
		playerActionPanel.setForeground(foregroundColor);
		playerActionPanel.setBackground(backgroundColor);

			//Hit Button
			JButton drawButton = new JButton("Hit");
			drawButton.setEnabled(false);
			drawButton.setFont(new Font("Sitka Small", Font.PLAIN, 30));
			drawButton.setBounds(33, 50, 200, 170);
			drawButton.addActionListener(e -> drawCard());
			drawButton.setName("drawButton");
			drawButton.setForeground(foregroundColor);
			drawButton.setBackground(specialColor);
			playerActionPanel.add(drawButton);

			//Stand Button
			JButton standButton = new JButton("Stand");
			standButton.setEnabled(false);
			standButton.setFont(new Font("Sitka Small", Font.PLAIN, 30));
			standButton.setBounds(731, 50, 200, 170);
			standButton.addActionListener(e -> stand());
			standButton.setName("standButton");
			standButton.setForeground(foregroundColor);
			standButton.setBackground(specialColor);
			playerActionPanel.add(standButton);

			//New Round Button
			JButton reloadButton = new JButton("New Round!");
			reloadButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
				}
			});
			reloadButton.setBounds(382, 231, 200, 21);
			reloadButton.addActionListener(e -> newRound());
			reloadButton.setName("reloadButton");
			reloadButton.setForeground(foregroundColor);
			reloadButton.setBackground(specialColor);
			playerActionPanel.add(reloadButton);

			//Double Down Button
			JButton doubleDownButton = new JButton("Double");
			doubleDownButton.setEnabled(false);
			doubleDownButton.setFont(new Font("Sitka Small", Font.PLAIN, 30));
			doubleDownButton.setBounds(265, 50, 200, 170);
			doubleDownButton.addActionListener(e -> doubleDown());
			doubleDownButton.setName("doubleDownButton");
			doubleDownButton.setForeground(foregroundColor);
			doubleDownButton.setBackground(specialColor);
			playerActionPanel.add(doubleDownButton);

			//Split Button
			JButton splitButton = new JButton("Split");
			splitButton.addActionListener(e -> split());
			splitButton.setName("splitButton");
			splitButton.setFont(new Font("Sitka Small", Font.PLAIN, 30));
			splitButton.setBounds(498, 50, 200, 170);
			splitButton.setEnabled(false);
			splitButton.setForeground(foregroundColor);
			splitButton.setBackground(specialColor);

			playerActionPanel.add(splitButton);

		//Player Hand Panel
		playerHandPanel = new JPanel();
		playerHandPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		playerHandPanel.setLayout(null);
		playerHandPanel.setBounds(10, 176, 964, 96);
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
		topLabel.setForeground(foregroundColor);
		frmBlackjackJavaClient.getContentPane().add(topLabel);

		//Player Money Panel
		playerMoneyPanel = new JPanel();
		playerMoneyPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		playerMoneyPanel.setLayout(null);
		playerMoneyPanel.setBounds(10, 101, 964, 64);
		playerMoneyPanel.setForeground(foregroundColor);
		playerMoneyPanel.setBackground(backgroundColor);
		frmBlackjackJavaClient.getContentPane().add(playerMoneyPanel);

			JLabel lblMoney = new JLabel("Money:");
			lblMoney.setFont(new Font("Tahoma", Font.PLAIN, 24));
			lblMoney.setBounds(10, 11, 78, 42);
			lblMoney.setName("moneyLabel");
			lblMoney.setForeground(foregroundColor);
			playerMoneyPanel.add(lblMoney);

			JLabel moneyLabel = new JLabel("5000???");
			moneyLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
			moneyLabel.setBounds(98, 11, 131, 42);
			moneyLabel.setName("money");
			moneyLabel.setForeground(foregroundColor);
			playerMoneyPanel.add(moneyLabel);

			//Spinner for Player Bet
			JSpinner spinner = new JSpinner();
			spinner.setFont(new Font("Tahoma", Font.PLAIN, 24));
			spinner.setBounds(718, 11, 236, 42);
			spinner.setName("betAmount");
			spinner.addChangeListener(e -> updateBet());
			//Colour
			spinner.getEditor().getComponent(0).setForeground(foregroundColor);
			spinner.getEditor().getComponent(0).setBackground(backgroundColor);
			spinner.setBorder(new LineBorder(foregroundColor, 1));
			int n = spinner.getComponentCount();
		    for (int i=0; i<n; i++)
		    {
		        Component c = spinner.getComponent(i);
		        if (c instanceof JButton)
		        {
		            c.setBackground(specialColor);
		        }
		    }
			playerMoneyPanel.add(spinner);

			JLabel spinnerLabel = new JLabel("Bet amount:");
			spinnerLabel.setHorizontalAlignment(SwingConstants.TRAILING);
			spinnerLabel.setBounds(565, 11, 143, 42);
			spinnerLabel.setName("betAmountLabel");
			spinnerLabel.setForeground(foregroundColor);
			spinnerLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
			playerMoneyPanel.add(spinnerLabel);

			lblGameStatus = new JLabel("");
			lblGameStatus.setBounds(10, 68, 966, 33);
			lblGameStatus.setHorizontalAlignment(SwingConstants.CENTER);
			lblGameStatus.setVerticalAlignment(SwingConstants.TOP);
			lblGameStatus.setName("gameStatusLabel");
			lblGameStatus.setForeground(new Color(136, 138, 145));
			lblGameStatus.setFont(new Font("Tahoma", Font.PLAIN, 24));
			frmBlackjackJavaClient.getContentPane().add(lblGameStatus);
	}

	protected void OnClose()
	{
		client.sendMessage("quit", false);
		if(this.gui2 != null)
		{
			this.gui2.OnClose();
		}
		frmBlackjackJavaClient.setVisible(false);
		frmBlackjackJavaClient.dispose();
		System.exit(0);
	}

	/**
	This method is called when the GUI window is resized.
	The method adjusts the size and positions of the GUI components to fit the new size of the window.
	*/
	protected void OnResize()
	{
		if (frmBlackjackJavaClient.getWidth() < 870)
		{
			frmBlackjackJavaClient.setBounds(frmBlackjackJavaClient.getX(), frmBlackjackJavaClient.getY(), 870, frmBlackjackJavaClient.getHeight());
		}

		System.out.println("#GUI# Resizing to fit frame size: width: " + frmBlackjackJavaClient.getWidth() + ", height: " + frmBlackjackJavaClient.getHeight());
		//Resize TopLabel
		topLabel.setBounds(topLabel.getX(), topLabel.getY(), frmBlackjackJavaClient.getWidth() - 20 - 16, topLabel.getHeight());
		lblGameStatus.setBounds(lblGameStatus.getX(), lblGameStatus.getY(), frmBlackjackJavaClient.getWidth() - 20 - 16, lblGameStatus.getHeight());

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
			playerMoneyPanel.setBounds(playerMoneyPanel.getX(), playerMoneyPanel.getY(), frmBlackjackJavaClient.getWidth() - 20 - 16, playerMoneyPanel.getHeight());

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
	/**
	The split method is used to perform a split action in a card game.
	It initializes a new GuiForSplit instance, calls the doSplit method from the client object and removes the second card from the player's card hand.
	It also disables the "splitButton" component, draws a card and resets the hand value.
	*/
	private void split()
	{
		System.out.println("#ClientGUI# Request to split Hand.");
		gui2 = new GuiForSplit(this, client, playerCardHand.get(1));
		client.doSplit(gui2);
		playerCardHand.remove(1);
		cardCnt--;
		updateCard(1, "card_back");

		//Disable Button
		Component[] actionComponents = playerActionPanel.getComponents();
		for (Component component : actionComponents)
		{
			if(component.getName().equals("splitButton"))
			{
				component.setEnabled(false);
			}
		}

		//Draw Automaticly & reset Value
		drawCard();
		handValue = this.client.getHandValue(false);
	}

	/**
	Adds the split money to the player's total money.
	@param splitMoney the amount of split money to be added
	*/
	public void addSplitMoney(int splitMoney)
	{
		money += splitMoney;

		Component[] components = playerMoneyPanel.getComponents();
		for (Component component : components)
		{
			if (component.getName().equals("money"))
			{
				JLabel label = (JLabel) component;
				label.setText(Integer.toString(money) + "???");
			}
		}
	}

	/**
	The doubleDown method is called when the player requests to double down.
	This method performs the following actions:
		Prints a message indicating that the player has requested to double down.
		Draws the last card.
		Doubles the player's bet.
		Updates the player's bet amount in the spinner.
		Repaints the frame.
		Automatically stands and ends the round.
	*/
	private void doubleDown()
	{
		System.out.println("#ClientGUI# Request to double down.");
		//Draw last Card
		drawCard();

		//double MoneyBet
		playerBet = playerBet * 2;

		//Update playerBet from spinner
		Component[] components = playerMoneyPanel.getComponents();
		for (Component component : components)
		{
			if (component.getName().equals("betAmount"))
			{
				JSpinner spinner = ((JSpinner) component);
				spinner.setValue(playerBet);
			}
		}
		frmBlackjackJavaClient.repaint();

		//Automaticly Stand & end Round
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stand();
	}

	/**
	The method drawCard() requests a new card from the server and updates the GUI accordingly.
	If the player already has 2 cards in their hand, the double down and split buttons will be disabled.
	*/
	private void drawCard()
	{
		System.out.println("#ClientGUI# Request new Card.");
		client.sendMessage("draw", false);

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

	/**
	The method {@code stand} is used to make the player stand and end the round of BlackJack.
	It disables all the buttons on the playerActionPanel except the reload button and enables the betSpinner.
	It sends a message "stand" to the server.
	*/
	private void stand()
	{
		System.out.println("#ClientGUI# Request to stand.");
		client.sendMessage("stand", false);

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

		//Enable betSpinner
		Component[] moneyComponents = playerMoneyPanel.getComponents();
		for (Component component : moneyComponents)
		{
			if (component.getName().equals("betAmount"))
			{
				component.setEnabled(true);
			}
		}
	}

	/**
	 * The method {@code newRound} resets the game state and prepares the game for a new round of blackjack.
	 * First, the method checks if the player has placed a valid bet (greater than 0 and less than or equal to their current money).
	 * Then, the method resets variables such as `cardCnt` and `handValue`, and updates the hand value with `updateHandValue`.
	 * The cards in the player's hand are reset and the back of the cards are displayed. The bet spinner is locked and the game status label is reset.
	 * The first two cards are then drawn and the buttons are reset. The "reload" button is set to be disabled.
	 * Finally, the Frame is repainted to update the display.
	 */
	private void newRound()
	{
		if (!(playerBet > 0 && playerBet <= money))
		{
			return;
		}

		System.out.println("#ClientGUI# Reset on Client.");
		//Reset variables
		cardCnt = 0;
		handValue = 0;
		updateHandValue();

		//Reset cards
		playerCardHand = new ArrayList<>();
		for (int i = 0; i < playerHand.size(); i++)
		{
			updateCard(i, "card_back");
		}

		//Lock betSpinner & Reset GameStatus
		Component[] moneyComponents = playerMoneyPanel.getComponents();
		for (Component component : moneyComponents)
		{
			if (component.getName().equals("betAmount"))
			{
				component.setEnabled(false);
			}
		}
		lblGameStatus.setText("");

		//Draw first two cards
		drawCard();

		synchronized(this) {
			while(playerCardHand.size() < 1 )
			{
				System.out.print("");
			}
		}

		drawCard();

		//Reset Buttons
		Component[] actionComponents = playerActionPanel.getComponents();
		for (Component component : actionComponents)
		{
			if(component.getClass() == JButton.class && !component.getName().equals("reloadButton") && !component.getName().equals("splitButton"))
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
		frmBlackjackJavaClient.repaint();
	}

	/**
	Method that updates the player's bet based on the value selected in the betAmount spinner.
	*/
	private void updateBet()
	{
		//Update playerBet from spinner
		Component[] components = playerMoneyPanel.getComponents();
		for (Component component : components)
		{
			if (component.getName().equals("betAmount"))
			{
				JSpinner spinner = ((JSpinner) component);
				playerBet = (int) spinner.getValue();
			}
		}
	}


	//PlayerLogic
	/**
	The method sets a card for the player and checks if the split-option would be valid.
	It also calls {@code updateCard} to update the card images.
	@param card the card to be set.
	*/
	public void setCard(Card card)
	{
		updateCard(cardCnt, card.getName());
		playerCardHand.add(card);
		handValue = this.client.getHandValue(false);
		cardCnt++;

		//Check for duplicate Cards to enable Split
		if (cardCnt == 2 && gui2 == null)
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

	/**
	Updates the displayed hand value of the player in the GUI.
	*/
	private void updateHandValue()
	{
		playerHandValueLabel.setText("Current player-hand value: " + handValue + "");
		frmBlackjackJavaClient.repaint();
	}

	/**
	This method updates the image for a card on the GUI.
	@param pos The position of the card in the player's hand.
	@param type The name of the card's image file.
	*/
	public void updateCard(int pos, String type)
	{
		JLabel card = playerHand.get(pos);
		//Gui	https://www.kenney.nl/assets/playing-cards-pack
		card.setIcon(new ImageIcon(Gui.class.getResource("/resources/" + type + ".png")));
		frmBlackjackJavaClient.repaint();
	}

	/**
	Updates the GUI to display the outcome of the round and the dealer's hand value.
	@param gameStatus the outcome of the round - either "win", "loose", or "draw"
	@param dealerHandValue the hand value of the dealer
	*/
	public void endRound(String gameStatus, int dealerHandValue)
	{
		System.out.println("#Client# Round state: " + gameStatus);
		String gameStatusMSG = "";
		switch (gameStatus)
		{
			case "win":
				money += playerBet * 1.5;
				gameStatusMSG = " You WON " + (int)(playerBet * 1.5) + "??? Dealer has:" + dealerHandValue;
				break;

			case "loose":
				money -= playerBet;
				gameStatusMSG = " You LOST " + playerBet + "??? Dealer has:" + dealerHandValue;
				break;

			case "draw":
				//Stays the same
				gameStatusMSG = " DRAW  Dealer has:" + dealerHandValue;
				break;
		}

		//Update GUI
		Component[] components = playerMoneyPanel.getComponents();
		for (Component component : components)
		{
			if (component.getName().equals("money"))
			{
				JLabel label = (JLabel) component;
				label.setText(Integer.toString(money) + "???");
			}
		}
		lblGameStatus.setText(gameStatusMSG);
	}
}
