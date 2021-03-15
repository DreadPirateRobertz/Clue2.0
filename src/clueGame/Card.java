package clueGame;

public class Card {
    public String getCardName() {
        return cardName;
    }

    public CardType getCardType() {
        return cardType;
    }

    private CardType cardType;
    private String cardName;

    public Card(CardType cardType, String cardName) {
        this.cardType = cardType;
        this.cardName = cardName;
    }

    public boolean equals(Card target){
        return true; //STUB
    }

}
