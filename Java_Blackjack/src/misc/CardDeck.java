package misc;

import java.util.ArrayList;
import java.util.Random;

public class CardDeck 
{
	public volatile ArrayList<Card> CardDeck = new ArrayList<Card>();
	
	private String[] cardNames	= {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
	private int[] cardValues 	= { 11	,  2 ,  3 ,  4 ,  5 ,  6 ,  7 ,  8 ,  9 ,  10 ,   10  ,   10   ,   10  };
	
	/**
	 * Dies ist ein Konstruktor, der bei der Erstellung eines CardDeck-Objekts aufgerufen wird und dazu verwendet wird, das Deck zu initialisieren und zu mischen.
	 * @author Rudi Wagner
	 */
	public CardDeck() 
	{	
		shuffle();
	}
	
	/**
	 * Diese Methode wählt eine zufällige Karte aus dem Deck aus und entfernt sie aus dem Deck. Falls das Deck leer ist, wird es gemischt.
	 * @return
	 */
	public Card drawCard()
	{
		if (CardDeck.isEmpty()) 
		{
			shuffle();
		}
		
		Random rand = new Random();
		int randomNum = rand.nextInt(CardDeck.size());
		
		//Retrieve Card according to random Int & remove it from the deck
		Card card = CardDeck.get(randomNum);
		CardDeck.remove(randomNum);
		return card;
	}

	/**
	 * Dies ist eine Getter-Methode, die die Größe des CardDeck zurückgibt.
	 * @return
	 */
	public int getCardDeckSize() 
	{
		return CardDeck.size();
	}

	/**
	 * Diese Methode mischt das Deck, indem es es zuerst löscht und dann mit neuen Karten wieder aufbaut.
	 */
	public void shuffle() 
	{
		CardDeck.clear();
		for (int i = 0; i < cardNames.length; i++) 
		{
			CardDeck.add(new Card(cardNames[i] + " of Hearts", cardValues[i]));
			CardDeck.add(new Card(cardNames[i] + " of Spades", cardValues[i]));
			CardDeck.add(new Card(cardNames[i] + " of Diamonds", cardValues[i]));
			CardDeck.add(new Card(cardNames[i] + " of Clover", cardValues[i]));
		}
	}
	
	
}