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
    public Suggestion createSuggestion(Room room, ArrayList<Card> allCards) {
        Random randomize = new Random();
        ArrayList<Card> weaponCards = new ArrayList<>();
        ArrayList<Card> personCards = new ArrayList<>();
        Card roomCard = new Card(CardType.ROOM, room.getName()); //Producing the roomCard
        for (Card card : allCards){//Splitting the deck into 2 smaller decks of Cards to randomize a suggestion
            if (card.getCardType().equals(CardType.PERSON) && !seenList.contains(card) && !playerHand.contains(card)){
                personCards.add(card);
            }
            else if (card.getCardType().equals(CardType.WEAPON) && !seenList.contains(card) && !playerHand.contains(card)){
                weaponCards.add(card);
            }
        }
        Collections.shuffle(personCards);
        Collections.shuffle(weaponCards);
        Card personCard = personCards.get(randomize.nextInt(personCards.size()));
        Card weaponCard = weaponCards.get(randomize.nextInt(weaponCards.size()));
        return new Suggestion(personCard, roomCard, weaponCard);
    }

    @Override
    public BoardCell selectTargets(ArrayList<BoardCell> targets) {
        boolean flag = false;
        Random randomize = new Random();
        ArrayList<BoardCell> roomList = new ArrayList<>();

        for (BoardCell target : targets){
            if (target.isRoomCenter() ){ //Is it a room?
                for (Card card : playerHand){
                    if (!card.getCardType().equals(CardType.ROOM)){
                        continue; //If not a room ....Advance the loop
                    }
                    char roomID1= target.getInitial();
                    char roomID2 = card.getCardName().charAt(0);
                    if (roomID1 != roomID2){
                        flag = true;
                    }
                    else {
                        flag = false;
                        break; //Break here is critical
                    }
                }
                if (flag){
                    roomList.add(target);
                }
            }
        } //Brain Dead AI logic follows to assign the correct move
        if (roomList.size() == 1){
            return roomList.get(0);
        }
        else if (roomList.size() > 1){
            Collections.shuffle(roomList);
            return roomList.get(randomize.nextInt(roomList.size()));
        }
        else {
            Collections.shuffle(targets);
            return targets.get(randomize.nextInt(targets.size()));
        }
    }





}