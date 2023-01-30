package jUnit;

import junit.framework.TestCase;
import misc.Card;

public class JUT_Card extends TestCase
{
	/**
	 * Check if Card is initialized correctly
	 */
	public void testCase_1()
	{
		String name = "Test123";
		int value = 5;
		
		Card card = new Card(name, value);
		
		assertNotNull(card);
		assertEquals(name, card.getName());
		assertEquals(value, card.getValue());
	}
}
