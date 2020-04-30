import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Game {



    private static Queue<Card> createQueue() {
        Deck.getDeck();
        ArrayList<Card> original = new ArrayList<>();
        for (int j = 0; j < 108; j++) {
            original.add(Deck.deck[j]);
        }
        Queue<Card> q = new LinkedBlockingQueue<>(original);
        return q;
    }

    public static Queue<Card> cardQueue = createQueue();

    public static Card[] dealCards() {
        Card[] hand1 = new Card[7];
        for (int i = 0; i < 7; i++) {
            Card card = cardQueue.remove();
            hand1[i] = card;
        }
       return hand1;
    }

}
