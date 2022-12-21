package jUnit;

import junit.framework.TestCase;
import misc.Card;
import misc.CardHand;

public class JUT_CardHand extends TestCase
{
	private CardHand cardHand;
	
	protected void setUp()
	{
		cardHand = new CardHand();
	}
	
	/*
	 * Basic tests on standard Methods
	 */
	public void testCase_1()
	{
		//Test if Object is created
		assertNotNull(cardHand);
		
		//Test if Card get added correctly
		cardHand.addCard(new Card("Test Card1", 2));
		cardHand.addCard(new Card("Test Card2", 2));
		cardHand.addCard(new Card("Test Card3", 2));
		cardHand.addCard(new Card("Test Card4", 2));
		assertEquals(4, cardHand.getCardHandSize());
		
		//Clear and test if cleared
		cardHand.clearHand();
		assertEquals(0, cardHand.getCardHandSize());
	}
	
	/*
	 * Test getValue() Method
	 * Normal Value without Aces
	 * 5 + 5 + 5 = 15
	 */
	public void testCase_2()
	{
		cardHand.clearHand();
		cardHand.addCard(new Card("Test Card1", 5));
		cardHand.addCard(new Card("Test Card2", 5));
		cardHand.addCard(new Card("Test Card3", 5));
		assertEquals(15, cardHand.getValue());
	}
	
	/*
	 * Test getValue() Method
	 * Normal Value with Ace at first place
	 * 11 + 5 + 5 = 21
	 */
	public void testCase_3()
	{
		cardHand.clearHand();
		cardHand.addCard(new Card("Test Card Ace", 11));
		cardHand.addCard(new Card("Test Card2", 5));
		cardHand.addCard(new Card("Test Card3", 5));
		assertEquals(21, cardHand.getValue());
	}
	
	/*
	 * Test getValue() Method
	 * Normal Value with Ace at last place
	 * 10 + 5 + 11 > 21 --> deswegen soll das Ass als 1 gewertet werden
	 * also 10 + 5 + 1 = 16
	 */
	public void testCase_4()
	{
		cardHand.clearHand();
		cardHand.addCard(new Card("Test Card1", 10));
		cardHand.addCard(new Card("Test Card2", 5));
		cardHand.addCard(new Card("Test Card Ace", 11));
		assertEquals(16, cardHand.getValue());
	}
	
	/*
	 * Test getValue() Method
	 * Normal Value with Ace in the middle
	 * 5 + 11 + 5 = 21
	 */
	public void testCase_5()
	{
		cardHand.clearHand();
		cardHand.addCard(new Card("Test Card1", 5));
		cardHand.addCard(new Card("Test Card Ace", 11));
		cardHand.addCard(new Card("Test Card3", 5));
		assertEquals(21, cardHand.getValue());
	}
}
