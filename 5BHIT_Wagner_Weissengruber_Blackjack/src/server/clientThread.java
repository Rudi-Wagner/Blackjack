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
	
	public clientThread(Socket givenSocket, CardDeck deck)
	{
		this.socket = givenSocket;
		this.deck = deck;
	}
	
    public void run() 
    { 
    	boolean run = true;
    	System.out.println("#ThreadLog# Thread " + this.getId() + " is now running!");
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
	                	System.out.println("#Thread# " + this.getId() + " Thread erhält nun Daten!");
	                }
	                else if(recievedMSG.equals("closeSession"))
	                { //Abbruch der Verbindung falls der Client das fordert
	                	run = false;
	                	System.out.println("#Thread# " + this.getId() + " Client requested shutdown!");
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
								msg = cardToJson(card);
								break;
							
							case "stand":
								int playerHandValue = jsondata.getValue();
								@SuppressWarnings("unused") int dealerHandValue = 22;
								//Vergleichen mit Server hand
								System.out.println("#ThreadLog# Player's Hand is " + playerHandValue + " worth!");
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
		System.out.println("#ThreadLog# Thread " + this.getId() + " is now stopping!");
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("#Log# Socket ~" + socket.getRemoteSocketAddress() + "~ in Thread " + this.getId() + " has been closed!");
    }

	private String cardToJson(Card card) 
	{
		Gson gson = new Gson();
		String jsondata = gson.toJson(card);
		return jsondata;
	}
}
