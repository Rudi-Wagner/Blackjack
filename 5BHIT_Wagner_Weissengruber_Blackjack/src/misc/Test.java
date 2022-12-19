package misc;

import java.util.Iterator;

public class Test {

	

	public static void main(String[] args) 
	{
		CardHand Test = new CardHand();
		Test.addCard(new Card("fett", 10));
		
		for (int i = 0; i < 5; i++) 
		{
			Card Rudi = new Card("Ace", 11);
			Test.addCard(Rudi);
			int value = Test.getValue();
			System.out.println(value);
			
		}

	}

}
