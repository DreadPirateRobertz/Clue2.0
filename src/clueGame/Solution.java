package clueGame;

public class Solution {

    public static Card person, room, weapon;

    public static void theAnswer(){
        System.out.println("The villain in our story is " + person.getCardName() +
                " and he/she did their dastardly deed in the " + room.getCardName() +
                " Room with the " + weapon.getCardName());
    }

}

