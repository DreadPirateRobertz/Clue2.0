package clueGame;

public class Solution {


    private static Card person, room, weapon;

    public static void theAnswer(){
        System.out.println("The villain in our story is " + person.getCardName() +
                " and he/she did their dastardly deed in the " + room.getCardName() +
                " Room with the " + weapon.getCardName());
    }

    public static void setPerson(Card person) {
        Solution.person = person;
    }

    public static void setRoom(Card room) {
        Solution.room = room;
    }

    public static void setWeapon(Card weapon) {
        Solution.weapon = weapon;
    }

    public static Card getPerson() {
        return person;
    }

    public static Card getRoom() {
        return room;
    }

    public static Card getWeapon() {
        return weapon;
    }

}

