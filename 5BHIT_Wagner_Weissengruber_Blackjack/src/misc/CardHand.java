package misc;

import java.util.ArrayList;

import com.google.gson.Gson;

public class CardHand 
{
	public ArrayList<Card> cardHand;

	/**
	 * Konstruktor, initialisiert eine neue ArrayList für die Karten.
	 */
	public CardHand() 
	{
		cardHand = new ArrayList<Card>();
	}

	/**
	 * Konvertiert die ArrayList von Karten in einen JSON-String und gibt diesen zurück.
	 * @return
	 */
	public String getCardHand() 
	{
		Gson gson = new Gson();
		String jsondata = gson.toJson(cardHand);
		return jsondata;
	}

	/**
	 * Fügt eine Karte zur ArrayList hinzu.
	 * @param card
	 */
	public void addCard(Card card) 
	{
		cardHand.add(card);
	}
	
	/**
	 * Gibt die Karte an der angegebenen Position in der ArrayList zurück.
	 * @param pos
	 * @return
	 */
	public Card getCardAt(int pos)
	{
		return cardHand.get(pos);
	}
	
	/**
	 * Gibt die Anzahl der Karten in der ArrayList zurück.
	 * @return
	 */
	public int getCardHandSize()
	{
		return cardHand.size();
	}
	
	/**
	 * Löscht alle Karten aus der ArrayList.
	 */
	public void clearHand()
	{
		cardHand.clear();
	}
	
	/**
	 * Gibt den Wert aller Karten in der Hand zurück. Hier werden spezielle Regeln für die Bewertung von Assen berücksichtigt.
	 * @return
	 */
	public int getValue() 
	{
		int value = 0;
		
		for (int i = 0; i < cardHand.size(); i++) 
		{
			Card card = cardHand.get(i);
			
			//Normal dazu rechnen
			if (value + card.getValue() <= 21) 
			{
				value += card.getValue();
			}
			else
			{
				//Hinzurechnen bei einem Ass
				if (card.getValue() == 11) 
				{
					value += 1;
				}
				else
				{
					value += card.getValue();
				}
			}
		}
		
		return value;
	}
}
