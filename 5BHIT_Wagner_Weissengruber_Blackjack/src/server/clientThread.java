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

public class clientThread extends Thread
{
	
	/*
	 * Rudi Wagner
	 * Paul Weissengruber
	 * 5 BHIT
	 * Blackjack
	 * clientThread
	 */
	
	private Socket socket;
	private CardDeck deck;
	private CardHand dealerHand;
	
	public clientThread(Socket givenSocket, CardDeck deck)
	{
		this.socket = givenSocket;
		this.deck = deck;
	}
	
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
	            	Thread.sleep(1000);
	                recievedMSG = reader.readLine();
	                
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
								
								createDealerHand();
								int dealerHandValue = dealerHand.getValue();
								
								//Vergleichen mit Server hand
								System.out.println("#ThreadLog# Player's Hand is " + playerHandValue + " worth!");
								
								String state = getGameState(playerHandValue, dealerHandValue);
								System.out.println(state);
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
            } catch (InterruptedException e) {
    			e.printStackTrace();
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

	private String getGameState(int playerHandValue, int dealerHandValue) 
	{
		System.out.println("D: " + dealerHandValue + ", P: " + playerHandValue);
		//If both have more than 21 or their Value is equal
		if ((dealerHandValue > 21 && playerHandValue > 21) ||
			(dealerHandValue == playerHandValue)) 
		{
			return "draw";
		}
		else
		//Player Win
		//Less than 22 and more than the dealer
		if (playerHandValue < 22 && playerHandValue > dealerHandValue) 
		{
			return "win";
		}
		else
		//Player Loose
		//More than 21 or less than the dealer
		if (playerHandValue > 21 || playerHandValue < dealerHandValue) 
		{
			return "loose";
		}
		
		//Couldn't calculate
		return "draw";
	}

	private void createDealerHand() 
	{
		dealerHand = new CardHand();
		
		while (dealerHand.getValue() <= 16) 
		{
			Card card = deck.drawCard();
			dealerHand.addCard(card);
		}
	}
}
