package misc;

import java.util.ArrayList;
import java.util.Random;

public class CardDeck 
{
    private volatile ArrayList<Card> cardDeck = new ArrayList<Card>();

    private String[] cardNames    = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
    private int[] cardValues     = { 11    ,  2 ,  3 ,  4 ,  5 ,  6 ,  7 ,  8 ,  9 ,  10 ,   10  ,   10   ,   10  };

    public CardDeck() 
    {
        //Initialize card deck
        shuffle();
    }

    public Card drawCard()
    {
        //Generate new Card Deck
        if (cardDeck.isEmpty()) 
        {
            shuffle();
        }

        //Calculate Random Number
        Random rand = new Random();
        int randomNum = rand.nextInt(cardDeck.size());

        //Retrieve Card according to random Int & remove it from the deck
        Card card = cardDeck.get(randomNum);
        cardDeck.remove(randomNum);
        return card;
    }

    public void shuffle()
    {
        System.out.println("#CardDeck# Shuffled");
        cardDeck.clear();
        if (cardDeck.isEmpty()) 
        {
            for (int i = 0; i < cardNames.length; i++) 
            {
                cardDeck.add(new Card(cardNames[i] + " of Hearts", cardValues[i]));
                cardDeck.add(new Card(cardNames[i] + " of Spades", cardValues[i]));
                cardDeck.add(new Card(cardNames[i] + " of Diamonds", cardValues[i]));
                cardDeck.add(new Card(cardNames[i] + " of Clover", cardValues[i]));
            }
        }
    }

    //Getter/Setter
    public int getCardDeckSize() {
        return cardDeck.size();
    }
}