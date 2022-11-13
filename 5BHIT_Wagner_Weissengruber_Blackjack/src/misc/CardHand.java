package misc;

import java.util.ArrayList;

import com.google.gson.Gson;

public class CardHand 
{
	public ArrayList<Card> Hand;

	public CardHand() 
	{
		Hand = new ArrayList<Card>();
	}

	public String getCardHand() 
	{
		Gson gson = new Gson();
		String jsondata = gson.toJson(Hand);
		return jsondata;
	}

	public void addCard(Card card) {
		Hand.add(card);
	}
	
	public int getValue()
	{
		int value = 0;
		
		for (int i = 0; i < Hand.size(); i++) 
		{
			Card card = Hand.get(i);
			
			//Check Ass logic
			//  Muss ein Ass sein,                  Wert der Karten muss über 21 gelangen
//			if (card.getName().startsWith("Ass") && value + 11 > 21) 
//			{
//				value += 1;
//			}
//			else
			{
				value += card.getValue();
			}
		}
		
		return value;
	}
}
