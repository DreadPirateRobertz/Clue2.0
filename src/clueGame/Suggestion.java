package clueGame;

public class Suggestion {
    private static Card person, room, weapon;

//    public Suggestion(Card person, Card room, Card weapon) {
//        Suggestion.person = person;
//        Suggestion.room = room;
//        Suggestion.weapon = weapon;
//    }

    public static void setPerson(Card person) {
        Suggestion.person = person;
    }

    public static void setRoom(Card room) {
        Suggestion.room = room;
    }

    public static void setWeapon(Card weapon) {
        Suggestion.weapon = weapon;
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