package clueGame;

public class Card {

    private CardType cardType;
    private String cardName;

    public Card(CardType cardType, String cardName) {
        this.cardType = cardType;
        this.cardName = cardName;
    }

    //Getters
    public String getCardName() { return cardName; }
    public CardType getCardType() { return cardType; }


    public boolean equals(Card target){
        return cardType.equals(target.cardType) && cardName.equals(target.getCardName());

    }

}
