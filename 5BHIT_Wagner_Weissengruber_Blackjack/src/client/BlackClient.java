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
	private Socket socket;
	private CardHand hand;
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
    	//Gui	https://www.kenney.nl/assets/playing-cards-pack
    	int port = 6868;
    	
    	
 
    	try {
    		//Intialize socket, output and writer
    		socket = new Socket(hostname, port);
            output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
            hand = new CardHand();
            
            while (true) {
            	System.out.print("");	//Ohne dem gehts nimma xD
            	if (inputString.equals("draw")) 
            	{
            		inputString = getMsgDraw();
                    writer.println(inputString);	//Send message to server
                    inputString = "";
     
                    InputStream input = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
     
                    String answer = reader.readLine();	//Server answer
                    Card card = translateFromJson(answer);
                    window.setCard(card);
                    
                    System.out.println("#Client# Antwort vom Server: " + answer);
				}
            	else if (inputString.equals("stop"))
            	{
            		writer.println(getMsgStand());
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

	private Card translateFromJson(String msg) 
	{
		Gson gson = new Gson();
		if (msg.contains("name") && msg.contains("value")) 
		{
			Card card = gson.fromJson(msg, Card.class);
			hand.addCard(card);
			return card;
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
	
	public void sendMessage(String msg)
	{
		inputString = msg;
	}
	
	private String getMsgDraw()
	{
		JsonObj obj = new JsonObj("draw", 0);
		Gson gson = new Gson();
		String jsondata = gson.toJson(obj);
		System.out.println(jsondata);
		return jsondata;
	}
	
	private String getMsgStand()
	{
		JsonObj obj = new JsonObj("stand", hand.getValue());
		Gson gson = new Gson();
		String jsondata = gson.toJson(obj);
		System.out.println(jsondata);
		return jsondata;
	}
}
