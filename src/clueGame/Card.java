package clueGame;

import java.awt.*;

public class Card {

    private CardType cardType;
    private String cardName;
    private Color color;

    public Card(CardType cardType, String cardName) {
        this.cardType = cardType;
        this.cardName = cardName;
    }

    //Getters
    public String getCardName() { return cardName; }
    public CardType getCardType() { return cardType; }

    public Color getColor() {
        return color;
    }

    //Setters
    public void setColor(Color color) {
        this.color = color;
    }

    public boolean equals(Card target){
        return cardType.equals(target.cardType) && cardName.equals(target.getCardName());

    }

}
