package clueGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Set;

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
        while(!cardsCopy.isEmpty()) {
            for (Card cardy : cards) {
                for (Card card : notSeen) {
                    if (card.equals(cardy)) {
                        notSeen.remove(card);
                        cardsCopy.remove(cardy);
                        break;
                    }
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
        Collections.shuffle(personCards);
        Collections.shuffle(weaponCards);
        return new Suggestion(personCards.get(randomize.nextInt(personCards.size())), roomCard, weaponCards.get(randomize.nextInt(weaponCards.size())));
    }

    @Override
    public BoardCell selectTargets() {
        boolean flag = false;
        Random randy = new Random();
        ArrayList<BoardCell> possibleMove = new ArrayList<>();
        int pathLength = randy.nextInt(6) + 1;
        BoardCell celly = Board.getInstance().getCell(row, col);
        Board.getInstance().calcTargets(celly, pathLength);
        ArrayList<BoardCell> targets = new ArrayList<>(Board.getInstance().getTargets());

        for (BoardCell target : targets){
            if(target.isRoomCenter() ){
                for(Card card : cards){
                    if(!card.getCardType().equals(CardType.ROOM)){
                        continue;
                    }
                    char roomID1= target.getInitial();
                    char roomID2 = card.getCardName().charAt(0);
                    if(roomID1 != roomID2){
                        flag = true;
                    }
                    else{
                        flag = false;
                        break; //Break here is critical
                    }
                }
                if(flag){
                    possibleMove.add(target);
                }
            }
        }
        if (possibleMove.size() == 1){
            return possibleMove.get(0);
        }
        else if (possibleMove.size() > 1){
            Collections.shuffle(possibleMove);
            return possibleMove.get(randy.nextInt(possibleMove.size()));
        }
        else{
            Collections.shuffle(targets);
            return targets.get(randy.nextInt(targets.size()));
        }
    }
}
