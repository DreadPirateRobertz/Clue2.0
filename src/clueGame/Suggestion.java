package clueGame;

public class Suggestion {
    private Card person, room, weapon;

    public Suggestion(){} //Non parameterized option offered and then manual setting of all variables can be done
    public Suggestion(Card person, Card room, Card weapon) {
        this.person = person;
        this.room = room;
        this.weapon = weapon;
    }
    public void setPersonCard(Card person) { this.person = person; }
    public void setRoomCard(Card room) { this.room = room; }
    public void setWeaponCard(Card weapon) { this.weapon = weapon; }
    public Card getPersonCard() { return person; }
    public Card getRoomCard() { return room; }
    public Card getWeaponCard() { return weapon; }
}