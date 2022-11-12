package Misc;

import java.util.ArrayList;
import java.util.Random;

public class CardDeck 
{
	public static ArrayList<Card> CardDeck = new ArrayList<Card>();
	
	private String[] cardNames	= {"Ass", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
	private int[] cardValues 	= { 11	,  2 ,  3 ,  4 ,  5 ,  6 ,  7 ,  8 ,  9 ,  10 ,   10  ,   10   ,   10  };
	
	public static void main(String[] args)
	{
		CardDeck deck = new CardDeck();
		for (int i = 0; i < 52; i++) 
		{
			Card card = deck.drawCard();
			System.out.println(i + ": " + card.getName() + " -> " + card.getValue());
		}
	}
	
	public CardDeck() 
	{
		//Initialize card deck
		for (int i = 0; i < cardNames.length; i++) 
		{
			CardDeck.add(new Card(cardNames[i] + " of Hearts", cardValues[i]));
			CardDeck.add(new Card(cardNames[i] + " of Spades", cardValues[i]));
			CardDeck.add(new Card(cardNames[i] + " of Diamonds", cardValues[i]));
			CardDeck.add(new Card(cardNames[i] + " of Clover", cardValues[i]));
		}
	}
	
	public Card drawCard()
	{
		//Calculate Random Number
		int max = CardDeck.size();
		Random rand = new Random();
		int randomNum = rand.nextInt(max);
		
		//Retrieve Card according to random Int & remove it from the deck
		Card card = CardDeck.get(randomNum);
		CardDeck.remove(randomNum);
		return card;
	}
}