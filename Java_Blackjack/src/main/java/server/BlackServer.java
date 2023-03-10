package main.java.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import main.java.misc.CardDeck;

/**
 * This is the main-Server it accepts the connection-requests from clients
 * and starts a new Thread {@code clientThread} to handle the connection.
 * @author Rudi Wagner
 */
public class BlackServer
{
	//ArrayLists um die Sockets und Threads zu "merken"
	private static ArrayList<Socket> allSockets = new ArrayList<>();
	private static ArrayList<Thread> allThreads = new ArrayList<>();


	/**
	 *	This is the Main-Server-Method to start the BlackjackServer.
	 *
		This is the main class of a server application that opens a socket to listen for clients.
		The server listens on a given port or 6868 and creates a new thread for each incoming client connection.
		The new thread takes over the communication with the client and operates on a shared deck of cards.
		If no clients are found within 3 seconds, a warning message will be displayed and the server will keep operating.
		If a client thread is stopped, it will be removed.
		If an exception occurs, the server will display a message and print a stack trace.

	 * @param args 1: port
	 *
	 */
	public static void main(String[] args)
	{
    	System.out.println("#Server# Server started!");

    	int port = 6868;		//Port festlegen
    	if(args.length > 0)
    	{
    		port = Integer.parseInt(args[0]);
    	}

    	CardDeck deck = new CardDeck();

    	//Oeffnen und warten auf Clients
    	try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("#Server# Server is listening on port " + port);
            Socket socket= null;
            serverSocket.setSoTimeout(3000);

            //Endlos Schleife um "unendlich" Clients aufzunehmen
            while (true) {
            	//Check fuer gestoppte Threads --> gestoppte Threads werden gel�scht
            	checkStoppedThread();
            	try {
            		socket = serverSocket.accept();
            	}catch(SocketTimeoutException ste)
            	{
            		System.out.println("#Warning# No Client found! Still operating!");	//Nach 3s wird neu gesucht
            	}

                if(socket != null)
                { //Wenn ein Client gefunden wurde, wird ein Thread gestartet der die Verbindung uebernimmt"
                	System.out.println("#Server# New client connected");
                	Thread clientRunnabel = new clientThread(socket, deck);
                	saveConnection(socket, clientRunnabel);
                	clientRunnabel.setName(socket.getInetAddress().toString());
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

	/**
	This method checks all threads in the list "allThreads" to see if they are alive.
	If a stopped thread is found, it will be removed from the list "allThreads" and its corresponding socket from the list "allSockets".
	*/
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

	/**
	This method saves a socket and its corresponding thread to the lists "allSockets" and "allThreads".
	The method also displays a log message indicating the current size of both lists.
	@param socket The socket to be saved
	@param thread The thread corresponding to the socket to be saved
	*/
    public static void saveConnection(Socket socket, Thread thread)
    {
    	allSockets.add(socket);
    	allThreads.add(thread);
    	System.out.println("#ServerLog# Socket and Thread saved, Sockets size: " + allSockets.size()
    							+ " , Threads size: " + allThreads.size());
    }

    /**
    This method removes a socket and its corresponding thread from the lists "allSockets" and "allThreads".
    The method also displays a log message indicating the current size of both lists.
    @param socket The socket to be removed
    @param thread The thread corresponding to the socket to be removed
    */
    public static void delConnection(Socket socket, Thread thread)
    {
    	allSockets.remove(socket);
    	allThreads.remove(thread);
    	System.out.println("#ServerLog# Socket and Thread deleted, Sockets size: " + allSockets.size()
		+ " , Treads size: " + allThreads.size());
    }
}