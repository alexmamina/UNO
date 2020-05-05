import javax.swing.*;

public class Protocol {
    //Keeps track of where we are in the game
    //Specifies the workflow of a game: with states. after a client does something, we move
    //to a different state
    public static final int BEGINNING = 0;
    public static final int UNO = 1;
    public static final int CHALLENGE = 2;
    public static final int MOVE = 3;


    public static int state = BEGINNING;


    public Info processInput(Info input) {

        //game started
        //uno pressed
        //challenge uno pressed
        //game ended
        //move
        Info output = null;
        if (state == BEGINNING) {
            output = new Info(Game.cardQueue,
                    Deck.getCard(GameInterface.played.getName()));
            //state = MOVE;
        } else if (state == MOVE) {
            //if input has message uno or challenge move accordingly
           if (input.getMessage().equals("uno")) state = UNO;
           else if (input.getMessage().equals("challenge")) state = CHALLENGE;

        } else if (state == UNO) {
            //show message dialog
        } else if (state == CHALLENGE) {
            //show message dialog, take 2 cards
        } else {
            System.out.println("what are you doing here");
        }
        return output;
    }
}
