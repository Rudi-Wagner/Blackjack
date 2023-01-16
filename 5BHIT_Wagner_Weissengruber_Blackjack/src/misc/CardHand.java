package misc;

import java.util.ArrayList;

import com.google.gson.Gson;

public class CardHand 
{
	public ArrayList<Card> cardHand;

	public CardHand() 
	{
		cardHand = new ArrayList<Card>();
	}

	public String getCardHand() 
	{
		Gson gson = new Gson();
		String jsondata = gson.toJson(cardHand);
		return jsondata;
	}

	public void addCard(Card card) 
	{
		cardHand.add(card);
	}
	
	public Card getCardAt(int pos)
	{
		return cardHand.get(pos);
	}
	
	public int getCardHandSize()
	{
		return cardHand.size();
	}
	
	public void clearHand()
	{
		cardHand.clear();
	}
	
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
