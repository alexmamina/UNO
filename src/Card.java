import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Card implements Serializable {

    ImageIcon image;
    String name;

    public Card(ImageIcon image, String name) {
        this.image = new ImageIcon(image.getImage().getScaledInstance(78,122, Image.SCALE_SMOOTH));
        this.name = name;
    }

    @Override
    public String toString() {
        return "Card " + name;
    }
}
