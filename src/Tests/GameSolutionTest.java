package Tests;

import clueGame.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class GameSolutionTest {
    // NOTE: I made Board static because I only want to set it up one
    // time (using @BeforeAll), no need to do setup before each test.
    private static Board board;
    private Card weapon, room, person, roomy, chokey, mustard, bomb, karen, gun;
    private Player player1, player2, player3;
    ArrayList<Card> cards;

    @BeforeAll
    public static void setUp() {
        // Board is singleton, get the only instance
        board = Board.getInstance();
        // set the file names to use my config files
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        // Initialize will load BOTH config files
        board.initialize();
    }
    @BeforeEach
    public void setUpp(){
        weapon = new Card(CardType.WEAPON, "Rusty Shank");
        room = new Card(CardType.ROOM, "ImmersiveVR");
        person = new Card(CardType.PERSON, "Rusty Shackleford");
        roomy = new Card(CardType.ROOM, "Brig");
        chokey = new Card(CardType.WEAPON, "Garrote");
        mustard = new Card(CardType.PERSON, "Colonel Mustard");
        bomb = new Card(CardType.WEAPON, "Bomb");
        gun = new Card(CardType.WEAPON, "Gun");
        karen = new Card(CardType.PERSON, "Karen");
        Suggestion.setWeapon(weapon);
        Suggestion.setRoom(room);
        Suggestion.setPerson(person);

        player1 = new Computer("larry", Color.red, "Hell");
        player2 = new Human("ShitStick", Color.orange, "Heaven");
        player3 = new Computer("AssGoblin", Color.pink, "Purgatory");
        cards = new ArrayList<>();
    }
    @Test
    public void testCheckAccusation() {//Tests all scenarios all correct or one of each is wrong for expected return
        Solution.setWeapon(weapon);
        Solution.setRoom(room);
        Solution.setPerson(person);
        assertTrue(board.checkAccusation(person, room, weapon));
        assertFalse(board.checkAccusation(mustard, room, weapon));
        assertFalse(board.checkAccusation(person, roomy, weapon));
        assertFalse(board.checkAccusation(mustard, room, chokey));

    }
    @Test
    public void testDisproveSuggestion1(){ //All match
        cards.add(weapon);
        cards.add(room);
        cards.add(person);
        player1.setMyCards(cards);
        assertNotNull(player1.disproveSuggestion());
    }
    @Test
    public void testDisproveSuggestion2(){//One match
        cards.add(chokey);
        cards.add(roomy);
        cards.add(person);
        player1.setMyCards(cards);
        assertEquals(person, player1.disproveSuggestion());
    }
    @Test
    public void testDisproveSuggestion3(){//No match
        cards.add(chokey);
        cards.add(roomy);
        cards.add(mustard);
        player1.setMyCards(cards);
        assertNull(player1.disproveSuggestion());
    }
    @Test
    public void testDisproveSuggestion4(){//One match
        cards.add(chokey);
        cards.add(room);
        cards.add(mustard);
        player1.setMyCards(cards);
        assertEquals(room, player1.disproveSuggestion());
    }
    @Test
    public void testHandleSuggestion1(){ //Accuser has all the cards to disprove so should be null
        Map<Player, ArrayList<Card>> temp = board.getPlayerMap();
        temp.clear();
        cards.add(gun);
        cards.add(roomy);
        cards.add(mustard);
        player1.setMyCards(cards);
        temp.put(player1, player1.getMyCards());
        ArrayList<Card> temp2 = new ArrayList<>(cards);
        temp2.clear();
        temp2.add(bomb);
        temp2.add(karen);
        temp2.add(chokey);
        player3.setMyCards(temp2);
        temp.put(player3, player3.getMyCards());
        ArrayList<Card> temp3 = new ArrayList<>(cards);
        temp3.clear();
        temp3.add(weapon);
        temp3.add(room);
        temp3.add(person);
        player2.setMyCards(temp3);
        temp.put(player2, player2.getMyCards());
        assertNull(board.handleSuggestion(player2));
    }
    @Test
    public void testHandleSuggestion2(){//One room card should be the only one returned by human player
        Map<Player, ArrayList<Card>> temp = board.getPlayerMap();
        temp.clear();
        cards.add(gun);
        cards.add(roomy);
        cards.add(mustard);
        player1.setMyCards(cards);
        temp.put(player1, player1.getMyCards());
        ArrayList<Card> temp2 = new ArrayList<>(cards);
        temp2.clear();
        temp2.add(bomb);
        temp2.add(karen);
        temp2.add(chokey);
        player3.setMyCards(temp2);
        temp.put(player3, player3.getMyCards());
        ArrayList<Card> temp3 = new ArrayList<>(cards);
        temp3.clear();
        temp3.add(gun);
        temp3.add(room);
        temp3.add(mustard);
        player2.setMyCards(temp3);
        temp.put(player2, player2.getMyCards());
        assertEquals(Suggestion.getRoom(), board.handleSuggestion(player3));
    }
    @Test
    public void testHandleSuggestion3(){//No one has answer return null
        Map<Player, ArrayList<Card>> temp = board.getPlayerMap();
        temp.clear();
        cards.add(gun);
        cards.add(roomy);
        cards.add(mustard);
        player1.setMyCards(cards);
        temp.put(player1, player1.getMyCards());
        ArrayList<Card> temp2 = new ArrayList<>(cards);
        temp2.clear();
        temp2.add(bomb);
        temp2.add(karen);
        temp2.add(chokey);
        player3.setMyCards(temp2);
        temp.put(player3, player3.getMyCards());
        ArrayList<Card> temp3 = new ArrayList<>(cards);
        temp3.clear();
        temp3.add(gun);
        temp3.add(chokey);
        temp3.add(mustard);
        player2.setMyCards(temp3);
        temp.put(player2, player2.getMyCards());
        assertNull(board.handleSuggestion(player1));
    }
    @Test
    public void testHandleSuggestion4(){//Two can disprove but first one in player order does so and returns
        Map<Player, ArrayList<Card>> temp = board.getPlayerMap();
        temp.clear();
        cards.add(gun);
        cards.add(roomy);
        cards.add(mustard);
        player1.setMyCards(cards);
        temp.put(player1, player1.getMyCards());
        ArrayList<Card> temp2 = new ArrayList<>(cards);
        temp2.clear();
        temp2.add(bomb);
        temp2.add(room);
        temp2.add(chokey);
        player2.setMyCards(temp2);
        temp.put(player2, player2.getMyCards());
        ArrayList<Card> temp3 = new ArrayList<>(cards);
        temp3.clear();
        temp3.add(gun);
        temp3.add(room);
        temp3.add(mustard);//
        player3.setMyCards(temp3);
        temp.put(player3, player3.getMyCards());
        ArrayList<Card> cardy = new ArrayList<>(temp.get(player2));
        assertEquals(cardy.get(1), board.handleSuggestion(player1));
    }
}
