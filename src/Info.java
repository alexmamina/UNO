import javax.swing.*;
import java.io.Serializable;
import java.util.Queue;

public class Info implements Serializable {
    private int numCards;
    private Card played;
    private String message;
    private Queue pile;



    public Info() {
        numCards = 0;
        played = new Card(new ImageIcon("/Users/alexmamina/Downloads/backofcard.jpeg"),"default");
        message = "";
    }
    public Info(Queue pile, Card played) {
        this.played = played;
        this.pile = pile;
        this.message = "";
    }

    public Info(int numCards, Card played, String message) {
        this.numCards = numCards;
        this.played = played;
        this.message = message;
    }

    @Override
    public String toString() {
        return numCards + " card played: " + played + "; message " + message + pile.toString();
    }

    public Queue getPile() {
        return pile;
    }

    public void setPile(Queue pile) {
        this.pile = pile;
    }

    public int getNumCards() {
        return numCards;
    }

    public void setNumCards(int numCards) {
        this.numCards = numCards;
    }

    public Card getPlayed() {
        return played;
    }

    public void setPlayed(Card played) {
        this.played = played;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
