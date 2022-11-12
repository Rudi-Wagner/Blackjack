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
import Misc.JsonObj;


public class BlackClient {
	
	private static OutputStream output;
	private static PrintWriter writer;
	private static Socket socket;
 
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
            int durchläufe = 5;

            while (i < durchläufe) {
            	msg = getMsg();
//                System.out.println("#Client# Package: " + i + " , Gesendet: " + sendJSON);
                writer.println(msg);	//Send message to server
 
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
                String answer = reader.readLine();	//Server answer
                
                
                
                System.out.println("#Client# Antwort vom Server: " + answer);
                i++;
            }
            shutdown();
        } catch (UnknownHostException ex) {
 
            System.out.println("#Client# Server not found: " + ex.getMessage());
 
        } catch (IOException ex) {
 
            System.out.println("#Client# I/O error: " + ex.getMessage());
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
	
	private static String getMsg()
	{
		JsonObj obj = new JsonObj("draw", 0);
		Gson gson = new Gson();
		String jsondata = gson.toJson(obj);
		System.out.println(jsondata);
		return jsondata;
	}
}
