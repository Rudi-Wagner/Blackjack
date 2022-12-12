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
		int sum = 0;
		
		for (int i = 0; i < Hand.size(); i++) 
		{												// ace calculator
			int currentValue = Hand.get(i).getValue();
			if(currentValue == 11)
			{
				// Wenn der Wert der Karten des Spielers bereits 21 übersteigt, ist der Wert des Ass immer 1
				if (currentValue > 21) {
				    sum += 1;
				}
				// Ansonsten ist der Wert des Ass 11, es sei denn, das Blatt des Spielers würde dadurch über 21 hinausgehen, in diesem Fall ist der Wert 1
				else {
				    sum += (currentValue + 11 > 21) ? 1 : 11;
				}
			}
			else
			{
				sum += currentValue;
			}
			
			  
		}
	
		return sum;
	}	
}
