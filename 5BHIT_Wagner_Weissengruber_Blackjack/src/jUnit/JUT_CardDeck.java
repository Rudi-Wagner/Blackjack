package jUnit;

import junit.framework.TestCase;
import misc.CardDeck;

public class JUT_CardDeck extends TestCase
{
	private CardDeck cardDeck;
	
	protected void setUp()
	{
		cardDeck = new CardDeck();
	}
	
	public void testCase_1()
	{
		//Test if CardDeck is created and correctly filled
		int expected = 52;
		int actual = cardDeck.getCardDeckSize();
		
		assertNotNull(actual);
		assertEquals(actual, 52);
		
		//Test if drawCard() works Correctly
		cardDeck.drawCard();
		actual = cardDeck.getCardDeckSize();
		expected = 51;
		assertEquals(actual, expected);
	}
	
	public void testCase_2()
	{
		//Clear deck and test if empty
		for (int i = 0; i < 52; i++) 
		{
			cardDeck.drawCard();
		}
		int actual = cardDeck.getCardDeckSize();
		int expected = 0;
		assertEquals(actual, expected);
		
		//Shuffle and test if full (52)
		cardDeck.shuffle();
		expected = 52; //Full CardDeck has 52 Cards
		actual = cardDeck.getCardDeckSize();
		assertEquals(actual, expected);
	}
}
