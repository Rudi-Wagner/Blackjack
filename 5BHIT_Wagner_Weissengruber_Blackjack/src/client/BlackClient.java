package client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.Gson;

import misc.Card;
import misc.CardHand;
import misc.JsonObj;


public class BlackClient extends Thread{
	
	private OutputStream output;
	private PrintWriter writer;
	private static Gui window;
	private static GuiForSplit window2;
	private Socket socket = null;
	private CardHand hand;
	private CardHand handSplitted;
	private boolean doForSplitted = false;
	private String inputString = "";
 
	/*
	 * Rudi Wagner
	 * Paul Weissengruber
	 * 5 BHIT
	 * Blackjack
	 * Client
	 */
	
	public static void main(String[] args)
	{
		BlackClient client = new BlackClient();
		window = new Gui(client);
		client.start();
	}
	
	
	public void run() 
	{	
    	System.out.println("#Client# Client Started!");
    	//Connection Data
    	String hostname = "localhost";
    	int port = 6868;
 
    	try {
    		//Intialize socket, output and writer
    		while(socket == null)
    		{
    			try {
    				socket = new Socket(hostname, port);
    			}
    			catch(IOException ex)
    			{
    				System.out.println("#Client# No server found!");
    				try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
    			}
    		}
    		System.out.println("#Client# Connected succesfully");
    		   		
            output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
            hand = new CardHand();
            handSplitted = new CardHand();
            
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            
            while (true) {
            	System.out.print("");	//Ohne dem gehts nimma xD
            	if (inputString.equals("draw")) 
            	{
            		if (!doForSplitted) 
            		{//Do for standard GUI
            			inputString = getMsgDraw();
                        writer.println(inputString);	//Send message to server
                        inputString = "";
         
                        String answer = reader.readLine();	//Server answer
                        Card card = translateCardFromJson(answer);
                        hand.addCard(card);
                        window.setCard(card);
                        
                        synchronized(window) {
                            window.notify();
                        }

                        System.out.println("#Client# Antwort vom Server: " + answer);
					}
            		else
            		{//Do for splitted GUI
            			inputString = getMsgDraw();
                        writer.println(inputString);	//Send message to server
                        inputString = "";
         
                        String answer = reader.readLine();	//Server answer
                        Card card = translateCardFromJson(answer);
                        handSplitted.addCard(card);
                        window2.setCard(card);
                        
                        synchronized(window2) {
                            window2.notify();
                        }
                        
                        System.out.println("#Client# Antwort vom Server: " + answer);
            		}
				}
            	else if (inputString.equals("stand"))
            	{
            		if (!doForSplitted) 
            		{//Do for standard GUI
						writer.println(getMsgStand(hand.getValue()));
						hand = new CardHand();
	            		inputString = "";
	            		
	            		String answer = reader.readLine();	//Server answer
	            		JsonObj msgObj = translateFromJson(answer);
	            		String gameStatus = msgObj.getType();
	            		int dealerHandValue = msgObj.getValue();
	            		window.endRound(gameStatus, dealerHandValue);
					}
            		else
            		{//Do for splitted GUI
            			writer.println(getMsgStand(handSplitted.getValue()));
            			handSplitted = new CardHand();
                		inputString = "";
                		
                		String answer = reader.readLine();	//Server answer
                		JsonObj msgObj = translateFromJson(answer);
                		String gameStatus = msgObj.getType();
                		int dealerHandValue = msgObj.getValue();
                		window2.endRound(gameStatus, dealerHandValue);
            		}
            		
            	}
            	else if (inputString.equals("quit"))
            	{
            		break;
            	}
            }
            shutdown();
        } catch (UnknownHostException ex) {
 
            System.out.println("#Client# Server not found: " + ex.getMessage());
 
        } catch (IOException ex) {
 
            System.out.println("#Client# I/O error: " + ex.getMessage());
        }
    }


	private Card translateCardFromJson(String msg) 
	{
		Gson gson = new Gson();
		if (msg.contains("name") && msg.contains("value")) 
		{
			Card card = gson.fromJson(msg, Card.class);
			return card;
		}
		return null;
	}
	
	private JsonObj translateFromJson(String msg) 
	{
		Gson gson = new Gson();
		if (msg.contains("type") && msg.contains("value")) 
		{
			JsonObj obj = gson.fromJson(msg, JsonObj.class);
			return obj;
		}
		return null;
	}

	public void shutdown()
	{ //Bei einem geplanten Verbindungsabbruch wird eine letzte Nachricht geschickt, der Server stoppt somit den Thread
    	try {
    		System.out.println("#Client# Requested shutdown!");
    		writer.println("closeSession");
    		
    		//Langer Abstand ist notwendig, da der Thread sonst nicht mehr den Abbruch lesen kann
            Thread.sleep(5000);				
            
			socket.close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String msg, boolean doForSplitted)
	{
		this.inputString = msg;
		this.doForSplitted = doForSplitted;
	}
	
	private String getMsgDraw()
	{
		JsonObj obj = new JsonObj("draw", 0);
		Gson gson = new Gson();
		String jsondata = gson.toJson(obj);
		return jsondata;
	}
	
	private String getMsgStand(int handValue)
	{
		JsonObj obj = new JsonObj("stand", handValue);
		Gson gson = new Gson();
		String jsondata = gson.toJson(obj);
		return jsondata;
	}
	
	public void doSplit(GuiForSplit gui)
	{
		//Set GUI
		window2 = gui;
		
		//Save Cards
		Card card1 = hand.getCardAt(0);
		Card card2 = hand.getCardAt(1);
		
		//Clear hands
		hand.clearHand();
		handSplitted.clearHand();
		
		//Set both hands
		hand.addCard(card1);
		handSplitted.addCard(card2);
	}
	
	public int getHandValue(boolean doForSplitted)
	{
		if (doForSplitted) 
		{
			return handSplitted.getValue();
		}
		return hand.getValue();
	}
	
}
