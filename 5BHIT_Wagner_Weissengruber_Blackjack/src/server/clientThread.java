package server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.Gson;

import misc.Card;
import misc.CardDeck;
import misc.CardHand;
import misc.JsonObj;

/**
 * This is a new Thread that gets started from the main-Server.
 * It handels the connection to one Client each.
 * @author Rudi Wagner
 * @author Paul Weissengruber
 */
public class clientThread extends Thread
{	
	private Socket socket;
	private CardDeck deck;
	private CardHand dealerHand;
	
	/**
	The constructor for the class "clientThread". It initializes the instance variables "socket" and "deck".
	@param givenSocket The socket to be assigned to the instance variable "socket"
	@param deck The deck of cards to be assigned to the instance variable "deck"
	*/
	public clientThread(Socket givenSocket, CardDeck deck)
	{
		this.socket = givenSocket;
		this.deck = deck;
	}
	
	/**
	The {@code run} method of the {@code clientThread} class.
	This method receives and processes incoming requests from a client.
	
	The incoming requests are read from the input stream and written to the output stream using a {@link BufferedReader} and a {@link PrintWriter}.
	The method uses a {@link Gson} object to deserialize the incoming JSON messages and to serialize the outgoing JSON messages.
	The method checks the type of the incoming request, either "draw" or "stand". If the type is "draw", the method retrieves a card from the deck and returns it to the client.
	If the type is "stand", the method calculates the hand value of the dealer, compares it with the hand value of the player, and returns the game state to the client.
	The method ends if the client requests a close session.
	*/
    public void run() 
    { 
    	boolean run = true;
    	System.out.println("#ThreadLog# Thread " + this.getName() + " is now running!");
    	while(run)
		{
    		try {
    			//Liest gesendete strings
    			InputStream input = socket.getInputStream();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	
	            OutputStream output = socket.getOutputStream();
	            PrintWriter writer = new PrintWriter(output, true);
	
	
	            String recievedMSG;
	            
	            do {
	                recievedMSG = reader.readLine();
	                if(recievedMSG.isBlank()) //Finished loop early if blank
	                {
	                	continue;
	                }
	                
	                if(recievedMSG.equals("startSession"))
	                { //Verbindungs start
	                	System.out.println("#Thread# " + this.getName() + " Thread erhaelt nun Daten!");
	                }
	                else if(recievedMSG.equals("closeSession"))
	                { //Abbruch der Verbindung falls der Client das fordert
	                	run = false;
	                	System.out.println("#Thread# " + this.getName() + " Client requested shutdown!");
	                	break;
	                }
	                else
	                { //Client Communication
	                	Gson gson = new Gson();
	            		JsonObj jsondata = gson.fromJson(recievedMSG, JsonObj.class);
	                	String msg = "";
	                	switch (jsondata.getType()) 
	                	{
							case "draw":
								Card card = deck.drawCard();
								msg = gson.toJson(card);
								break;
							
							case "stand":
								int playerHandValue = jsondata.getValue();
								
								createDealerHand(playerHandValue);
								int dealerHandValue = dealerHand.getValue();
								
								//Vergleichen mit Server hand
								System.out.println("#ThreadLog# Player's Hand is " + playerHandValue + " worth!");
								
								String state = getGameState(playerHandValue, dealerHandValue);
								msg = gson.toJson(new JsonObj(state, dealerHandValue));
								
								break;
								
							default:
								throw new IllegalArgumentException("Unexpected value: " + jsondata.getType());
						}
	                	writer.println(msg);
	                }
	                
	            } while (true);
	            break;
    		} catch (IOException ex) {
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
    		}
		}
		System.out.println("#ThreadLog# Thread " + this.getName() + " is now stopping!");
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("#Log# Socket ~" + socket.getRemoteSocketAddress() + "~ in Thread " + this.getName() + " has been closed!");
    }

    /**
     * Determines the outcome of a game of Blackjack based on the values of the player's and dealer's hands.
     *
     * @param playerHandValue the sum of the player's hand
     * @param dealerHandValue the sum of the dealer's hand
     * @return the outcome of the game as a string: "win", "loose", or "draw"
     */
    private String getGameState(int playerHandValue, int dealerHandValue) 
	{
		System.out.println("D: " + dealerHandValue + ", P: " + playerHandValue);
		//If both have more than 21 or their Value is equal
		if (dealerHandValue == playerHandValue)
		{
			return "draw";
		}
		else
		//Player Win
		//Less than 22 and more than the dealer
		if (playerHandValue == 21 || playerHandValue > dealerHandValue && playerHandValue <= 21 || dealerHandValue > 21) 
		{
			return "win";
		}
		else
		//Player Loose
		//More than 21 or less than the dealer
		if (21 < playerHandValue || playerHandValue < dealerHandValue && dealerHandValue <= 21)
		{
			return "loose";
		}
		
		//Couldn't calculate
		return "draw";
	}

    /**
    Creates the dealer's hand and adds cards to it until the hand's value is greater than 16.
    @param playerHandValue The value of the player's hand to check whether it is reasonable to draw more cards.
    */
    private void createDealerHand(int playerHandValue) 
	{
		dealerHand = new CardHand();
		//Draw first two
		dealerHand.addCard(deck.drawCard());
		dealerHand.addCard(deck.drawCard());
		
		if(playerHandValue > 21)
		{
			return;
		}
		
		while (dealerHand.getValue() <= 16) 
		{
			Card card = deck.drawCard();
			dealerHand.addCard(card);
		}
	}
}
