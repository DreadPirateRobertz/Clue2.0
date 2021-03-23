package clueGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Computer extends Player {

    public Computer(String name, Color color, String startLocation) {
        super(name, color, startLocation);
    }

    @Override
    public Suggestion createSuggestion() {
        Random randomize = new Random();
        ArrayList<Card> notSeen = new ArrayList<>(Board.getInstance().getAllCards()); //Deep copy made so as not to effect allCards
        ArrayList<Card> cardsCopy = new ArrayList<>(cards); //Deep copy made so as not to effect allCards
        ArrayList<Card> weaponCards = new ArrayList<>();
        ArrayList<Card> personCards = new ArrayList<>();
        while(!cardsCopy.isEmpty())
        for(Card cardy : cards){
            for(Card card : notSeen){
                if(card.equals(cardy)){
                    notSeen.remove(card);
                    cardsCopy.remove(cardy);
                    break;
                }
            }
        }
        BoardCell celly = Board.getInstance().getCell(row, col);
        Room room = Board.getInstance().getRoom(celly);
        Card roomCard = new Card(CardType.ROOM, room.getName());
        for(Card card : notSeen){
            if(card.getCardType().equals(CardType.PERSON)){
                personCards.add(card);
            }
            else if (card.getCardType().equals(CardType.WEAPON)){
                weaponCards.add(card);
            }
        }
        return new Suggestion(personCards.get(randomize.nextInt(personCards.size())), roomCard, weaponCards.get(randomize.nextInt(weaponCards.size())));
    }

    @Override
    public BoardCell selectTargets() {
        return null;
    }


}
