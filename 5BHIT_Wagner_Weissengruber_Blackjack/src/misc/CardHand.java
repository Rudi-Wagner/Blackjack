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
		int getvalue = 0;

		// Iterate over each card in the hand
		for (Card card : cardHand) {
		  // If the card is an Ace, add 11 to the total value
		  if (card.getName().equals("Ace")) {
			  getvalue += 11;
		  }
		  // Otherwise, add the value of the card to the total value
		  else {
			  getvalue += card.getValue();
		  }
		}

		// If the total value of the hand is greater than 21 and there is an Ace
		// in the hand, subtract 10 from the total value (since the Ace can count as
		// either 1 or 11)
		if (getvalue > 21) {
		  for (Card card : cardHand) {
		    if (card.getName().equals("Ace")) {
		    	getvalue -= 10;
		    
		    }
		  }
		}
		return getvalue;	
	}
}
