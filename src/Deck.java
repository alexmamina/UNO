import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;

public class Deck {

    public static Card[] deck = new Card[108];

    public static ImageIcon back =
            new ImageIcon((new ImageIcon("backofcard.jpg")).
                    getImage().getScaledInstance(78,122, Image.SCALE_SMOOTH));

    public static Card[] getDeck() {
            File unoFolder = new File("UNO");
            File[] pictures = unoFolder.listFiles();
            int i = 0;
            if (pictures != null) {
                for (File card : pictures) {
                    //System.out.println(card);
                    if (!card.getName().equals(".DS_Store")) {
                        ImageIcon cardPic = new ImageIcon(card.getAbsolutePath());
                        Card c = new Card(cardPic, card.getName());
                        //Add card to deck
                        deck[i] = c;
                        i++;
                        //All cards that aren't 0 appear twice
                        if (!c.name.contains("0")) {
                            deck[i] = c;
                            i++;
                        }
                        //Black cards appear 4 times
                        if (c.name.contains("black")) {
                            deck[i] = c;
                            deck[i + 1] = c;
                            i += 2;
                        }
                    }
                }
            } else throw new NullPointerException("No pictures in that folder");
        shuffleDeck();
        return deck;
    }

    private static Card[] shuffleDeck() {
        ArrayList<Card> original = new ArrayList<>();
        for (int j = 0; j < 108; j++) {
            original.add(deck[j]);
        }
        Collections.shuffle(original);
        for (int j = 0; j < 108; j++) {
            deck[j] = original.get(j);
        }
        return deck;
    }


    public static String getCardName(ImageIcon image) {
        String result = "";
        for (Card c : deck) {
            if (c.image.equals(image)) {
                result = c.name;
            }

        }
        return result;
    }

    public static Card getCard(String n) {
        for (Card c : deck) {
            if (c.name.equals(n)) {
                return c;
            }
        }
        return null;
    }
}
