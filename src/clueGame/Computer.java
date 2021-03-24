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
    public Suggestion createSuggestion() { //
        Random randomize = new Random();
        ArrayList<Card> notSeen = new ArrayList<>(Board.getInstance().getAllCards()); //Deep copy made so as not to effect allCards
        ArrayList<Card> cardsCopy = new ArrayList<>(cards);
        ArrayList<Card> weaponCards = new ArrayList<>();
        ArrayList<Card> personCards = new ArrayList<>();
        while(!cardsCopy.isEmpty()) {
                for (Card seenCard : cards) {
                    for (Card card : notSeen) { //Logic deciphers which cards have been seen
                    if (card.equals(seenCard)) {
                        notSeen.remove(card);
                        cardsCopy.remove(seenCard);
                        break;
                    }
                }
            }
        }
        BoardCell celly = Board.getInstance().getCell(row, col);
        Room room = Board.getInstance().getRoom(celly);
        Card roomCard = new Card(CardType.ROOM, room.getName()); //Producing the roomCard
        for(Card card : notSeen){//Splitting the notSeen deck into 2 smaller decks of Cards to randomize a suggestion
            if(card.getCardType().equals(CardType.PERSON)){
                personCards.add(card);
            }
            else if (card.getCardType().equals(CardType.WEAPON)){
                weaponCards.add(card);
            }
        }
        Collections.shuffle(personCards);
        Collections.shuffle(weaponCards);
        Card personCard = personCards.get(randomize.nextInt(personCards.size()));
        Card weaponCard =  weaponCards.get(randomize.nextInt(weaponCards.size()));
        return new Suggestion(personCard, roomCard, weaponCard);
    }

    @Override
    public BoardCell selectTargets() {
        boolean flag = false;
        Random randomize = new Random();
        ArrayList<BoardCell> possibleMove = new ArrayList<>();
        int pathLength = randomize.nextInt(6) + 1;
        BoardCell thisCell = Board.getInstance().getCell(row, col);

        Board.getInstance().calcTargets(thisCell, pathLength);

        ArrayList<BoardCell> targets = new ArrayList<>(Board.getInstance().getTargets());//Deep copy of the set Targets
                                                                                        //Which I put into an array list to manipulate and later shuffle
        for (BoardCell target : targets){
            if(target.isRoomCenter() ){ //Is it a room?
                for(Card card : cards){
                    if(!card.getCardType().equals(CardType.ROOM)){
                        continue; //If not a room ....Advance the loop
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
        }                                       //Brain Dead AI logic follows to assign the correct move
        if (possibleMove.size() == 1){
            return possibleMove.get(0);
        }
        else if (possibleMove.size() > 1){
            Collections.shuffle(possibleMove);
            return possibleMove.get(randomize.nextInt(possibleMove.size()));
        }
        else{
            Collections.shuffle(targets);
            return targets.get(randomize.nextInt(targets.size()));
        }
    }
}
