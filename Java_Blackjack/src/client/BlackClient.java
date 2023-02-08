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


/**
 * This is the Client, to the GUI, that connects to a Server.
 * It is responsible to send and recieve data to/from the Server.
 * The messaging-system works with JSON.
 * @author Rudi Wagner
 */
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
	
	/**
	The main method that starts the game by creating an instance of the {@link BlackClient} class and the {@link Gui} class.
	@param args the command line arguments
	*/
	public static void main(String[] args)
	{
		BlackClient client = new BlackClient();
		window = new Gui(client);
		client.start();
	}
	
	/**
	The {@code run} method of the {@code BlackClient} class is responsible for connecting the client to the server, handling communication between the client and server, and processing the game logic.
	The method first outputs a message to indicate that the client has started.
	Then it sets up the connection to the server using the localhost and port 6868.
	If the server is not found, it retries to connect every 5 seconds until it succeeds.
	Once connected, the method initializes the socket, output, and writer, and creates a CardHand object to store the client's cards.
	The method also creates an input stream and a buffered reader to receive messages from the server.
	The game logic is processed in an infinite loop that listens for the input string to be either "draw", "stand", or "quit".
	If the input string is "draw", the client sends a message to the server to draw a card, receives the answer from the server, and updates the GUI.
	If the input string is "stand", the client sends a message to the server to stand, receives the answer from the server, and updates the GUI.
	If the input string is "quit", the loop is broken, and the method calls the shutdown method to close the socket.
	If any exceptions are caught, such as UnknownHostException or IOException, the method outputs an error message.
	*/
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


	/**
	 * Translates a JSON formatted string into a Card object.
	 *
	 * @param msg The JSON formatted string to be translated.
	 * @return A Card object if the string contains both "name" and "value", null otherwise.
	 */
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
	
	/**
	Translates a given JSON formatted string into a {@link JsonObj} object.
	@param msg The JSON formatted string to translate.
	@return The {@link JsonObj} object that was translated from the given JSON string.
	Returns {@code null} if the JSON string does not contain both "type" and "value" fields.
	*/
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

	/**
	Shuts down the client connection.
	Sends a closeSession message to the server before shutting down the connection.
	A sleep of 5000 milliseconds is necessary to ensure the thread can read the disconnection before it closes.
	*/
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
	
	/**
	Sends a message to the Client-Loop.
	@param msg The message to be sent.
	@param doForSplitted Determines if the message should be handled for splitGUI.
	*/
	public void sendMessage(String msg, boolean doForSplitted)
	{
		this.inputString = msg;
		this.doForSplitted = doForSplitted;
	}
	
	/**
	 * Converts the Draw action to JSON format.
	 *
	 * @return the JSON format of the Draw action
	 */
	private String getMsgDraw()
	{
		JsonObj obj = new JsonObj("draw", 0);
		Gson gson = new Gson();
		String jsondata = gson.toJson(obj);
		return jsondata;
	}
	
	/**
	Generates a JSON string message representing the player's decision to stand in the game.
	@param handValue the current value of the player's hand
	@return the JSON string message to be sent to the server
	*/
	private String getMsgStand(int handValue)
	{
		JsonObj obj = new JsonObj("stand", handValue);
		Gson gson = new Gson();
		String jsondata = gson.toJson(obj);
		return jsondata;
	}
	
	/**
	Method to split the players hand into two hands.
	It also sets the Object for the second gui-window
	@param gui the GUI that displays the split
	*/
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
	
	/**
	Gets the value of the hand.
	@param doForSplitted a boolean indicating if it's for the splitted hand or not
	@return the value of the hand
	*/
	public int getHandValue(boolean doForSplitted)
	{
		if (doForSplitted) 
		{
			return handSplitted.getValue();
		}
		return hand.getValue();
	}
	
}
