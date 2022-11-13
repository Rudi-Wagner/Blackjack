package client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.Gson;

import misc.Card;
import misc.CardHand;
import misc.JsonObj;


public class BlackClient {
	
	private static OutputStream output;
	private static PrintWriter writer;
	private static Socket socket;
	private static CardHand hand;
 
	/*
	 * Rudi Wagner
	 * Paul Weissengruber
	 * 5 BHIT
	 * Blackjack
	 * Client
	 */
	
	public static void main(String[] args) 
	{	
    	System.out.println("#Client# Client Started!");
    	//Connection Data
    	String hostname = "localhost";
    	int port = 6868;
 
    	try {
    		//Intialize socket, output and writer
    		socket = new Socket(hostname, port);
            output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
 
            String msg;
            int i = 0;
            int durchläufe = 26;
            hand = new CardHand();
            
            Scanner scanner = new Scanner(System. in);
            

            while (true) {
            	String inputString = scanner.nextLine();
            	
            	if (inputString.equals("draw")) 
            	{
            		msg = getMsgDraw();
                    writer.println(msg);	//Send message to server
     
                    InputStream input = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
     
                    String answer = reader.readLine();	//Server answer
                    translateFromJson(answer);
                    
                    
                    System.out.println("#Client# Antwort vom Server: " + answer);
				}
            	else if (inputString.equals("stop"))
            	{
            		break;
            	}
                i++;
            }
            writer.println(getMsgStand());
            shutdown();
        } catch (UnknownHostException ex) {
 
            System.out.println("#Client# Server not found: " + ex.getMessage());
 
        } catch (IOException ex) {
 
            System.out.println("#Client# I/O error: " + ex.getMessage());
        }
    }

	private static void translateFromJson(String msg) 
	{
		Gson gson = new Gson();
		if (msg.contains("name") && msg.contains("value")) 
		{
			Card card = gson.fromJson(msg, Card.class);
			hand.addCard(card);
		}
	}

	public static void shutdown()
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
	
	private static String getMsgDraw()
	{
		JsonObj obj = new JsonObj("draw", 0);
		Gson gson = new Gson();
		String jsondata = gson.toJson(obj);
		System.out.println(jsondata);
		return jsondata;
	}
	
	private static String getMsgStand()
	{
		JsonObj obj = new JsonObj("stand", hand.getValue());
		Gson gson = new Gson();
		String jsondata = gson.toJson(obj);
		System.out.println(jsondata);
		return jsondata;
	}
}
