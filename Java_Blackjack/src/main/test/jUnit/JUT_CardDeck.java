package main.test.jUnit;

import junit.framework.TestCase;
import main.java.misc.CardDeck;

public class JUT_CardDeck extends TestCase
{
	private CardDeck cardDeck;

	/**
	 * Initialisiert eine neue Instanz von "CardDeck".
	 */
	@Override
	protected void setUp()
	{
		cardDeck = new CardDeck();
	}

	/**
	 * Überprüft, ob die Instanz von "CardDeck" korrekt erstellt wurde und 52 Karten enthält, und ob die Methode drawCard() funktioniert und eine Karte aus dem Deck entfernt.
	 */
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

	/**
	 * Überprüft, ob das Deck leer ist, nachdem alle Karten entfernt wurden, und ob das Deck durch die Methode shuffle() neu gemischt und voll (52 Karten) wurde.
	 */
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
