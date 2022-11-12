package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import Misc.CardDeck;

/*
 * Rudi Wagner
 * Paul Weissengruber
 * 5 BHIT
 * Blackjack
 * Server
 */
public class BlackServer 
{ 
	//ArrayLists um die Sockets und Threads zu "merken"
	private static ArrayList<Socket> allSockets = new ArrayList<Socket>();
	private static ArrayList<Thread> allThreads = new ArrayList<Thread>();
	
	public static void main(String[] args) 
	{
    	System.out.println("#Server# Server started!");
    	int port = 6868;		//Port festlegen
    	CardDeck deck = new CardDeck();
 
    	//Öffnen und warten auf Clients
    	try (ServerSocket serverSocket = new ServerSocket(port)) {
    		 
            System.out.println("#Server# Server is listening on port " + port);
            Socket socket= null;
            serverSocket.setSoTimeout(3000);
 
            //Endlos Schleife um "unendlich" Clients aufzunehmen
            while (true) {
            	//Check für gestoppte Threads --> gestoppte Threads werden gelöscht
            	checkStoppedThread();
            	checkCardDeck();
            	try {
            		socket = serverSocket.accept();
            	}catch(SocketTimeoutException ste)
            	{
//            		System.out.println("#Warning# No Client found!");	//Nach 3s wird neu gesucht
            	}
        
                if(socket != null)
                { //Wenn ein Client gefunden wurde, wird ein Thread gestartet der die Verbindung "übernimmt"
                	System.out.println("#Server# New client connected");
                	Thread clientRunnabel = new clientThread(socket, deck);
                	saveConnection(socket, clientRunnabel);
                	clientRunnabel.start();
                	socket = null;
                	System.out.println("#Server# Waiting for next client on port " + port);
                }
            }
    	} catch (IOException ex) {
            System.out.println("#Server# Server exception: " + ex.getMessage());
            ex.printStackTrace();
    	}
    }
    
    private static void checkCardDeck() {
		// TODO Auto-generated method stub
		
	}

	public static void checkStoppedThread() 
    {	//Wenn ein gestoppter Thread gefunden wird, wird er aus der ArrayList entfernt
    	for (int i = 0; i < allThreads.size(); i++) 
    	{
			Thread thread = allThreads.get(i);
			Socket socket = allSockets.get(i);
			if(!thread.isAlive())
			{
				delConnection(socket, thread);
			}
		}
    }
    
    public static void saveConnection(Socket socket, Thread thread) 
    {
    	allSockets.add(socket);
    	allThreads.add(thread);
    	System.out.println("#ServerLog# Socket and Thread saved, Sockets size: " + allSockets.size() 
    							+ " , Threads size: " + allThreads.size());
    }
    
    public static void delConnection(Socket socket, Thread thread) 
    {
    	allSockets.remove(socket);
    	allThreads.remove(thread);
    	System.out.println("#ServerLog# Socket and Thread deleted, Sockets size: " + allSockets.size() 
		+ " , Treads size: " + allThreads.size());
    }
}