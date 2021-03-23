package clueGame;

public class Suggestion {
    private Card person, room, weapon;

    public Suggestion(Card person, Card room, Card weapon) {
        this.person = person;
        this.room = room;
        this.weapon = weapon;
    }
    public Suggestion(){ //Non parameterized option offered and then manual setting of all variables can be done

    }

    public void setPerson(Card person) {
        this.person = person;
    }

    public void setRoom(Card room) {
        this.room = room;
    }

    public void setWeapon(Card weapon) {
        this.weapon = weapon;
    }

    public Card getPerson() {
        return person;
    }

    public Card getRoom() {
        return room;
    }

    public Card getWeapon() {
        return weapon;
    }

}