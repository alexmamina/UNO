import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class GameInterface extends JFrame {
    //TODO add leaderboard and save scores to file

    static String color = "";
    JLabel numcards;
    JButton pile;
    static JButton played;
    ArrayList<Card> playedCards = new ArrayList<>();
    static JPanel handpanel = new JPanel();
    ActionListener go = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();
            Card lastCard = Deck.getCard(played.getName());
            Card newCard = Deck.getCard(b.getName());

            System.out.println(played.getName());
            boolean sameColor = color.equals(newCard.name.substring(0,3));
            boolean sameType = lastCard.name.substring(3,4).equals(
                    newCard.name.substring(3,4));
            JPanel parent = (JPanel) b.getParent();
            if (sameColor || sameType || newCard.name.contains("black")) {
                played.setIcon(b.getIcon());
                played.setName(b.getName());
                playedCards.add(newCard);
                parent.remove(b);
                parent.revalidate();
                parent.repaint();
                numcards.setText("Cards left: "+ handpanel.getComponents().length);
                color = newCard.name.substring(0,3);

                if (newCard.name.contains("black")) {
                    String[] cols = {"Green", "Blue", "Yellow", "Red"};
                    int inp = JOptionPane.showOptionDialog(null,"Select the colour you want",
                            "Choose colour",JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                            null, cols, null);
                    switch (inp) {
                        case 3:
                            JOptionPane.showMessageDialog(null, "Red");
                            color = "red";
                            break;
                        case 2:
                            JOptionPane.showMessageDialog(null, "Yellow");
                            color = "yel";
                            break;
                        case 1:
                            JOptionPane.showMessageDialog(null, "Blue");
                            color = "blu";
                            break;
                        case 0:
                            JOptionPane.showMessageDialog(null, "Green");
                            color = "gre";
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Wrong number");
                   }
                }
            } else JOptionPane.showMessageDialog(null, "Wrong card!");
            if (parent.getComponents().length == 0) {
                String[] ops = {"New game", "Quit"};
                int ans = JOptionPane.showOptionDialog(null, "You have won!","The end",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null, ops, "Quit");
                if (ans == JOptionPane.YES_OPTION) newGame();
                else closeWindow();
            }
        }
    };

    public GridBagConstraints constraints = new GridBagConstraints();

    public GameInterface(int client) {
        setSize(800,600);
        setTitle("UNO");
        setBackground(Color.white);
        setForeground(Color.white);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setTopPart();
        setPiles(client);
        addMenu();
        setHand();
        Game.serverInfos.add(new Info(Game.cardQueue, Deck.getCard(played.getName())));
    }

    void addMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenu gamemode = new JMenu("Game mode");

        JMenuItem rules = new JMenuItem("Game rules");
        rules.addActionListener(e -> {
            Desktop desktop = Desktop.getDesktop();
            URI rulesWeb;
            try {
                rulesWeb = new URI("https://en.wikipedia.org/wiki/Uno_(card_game)#Official_rules");
                desktop.browse(rulesWeb);
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        JMenuItem scores = new JMenuItem("Card points");
        scores.addActionListener(e -> {
            String[][] r = {{"0-9", "0-9"}, {"Stop, Reverse, +2","20"},{"Black","50"}};
            Object[] c = {"Card", "Score"};
            JTable scores1 = new JTable(r,c);
            String[] options = {"OK", "Calculate for me"};
            int calculateScore = JOptionPane.showOptionDialog(null,(new JScrollPane(scores1)),
                    "Scores",
                    JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE,null,options,
                    "OK");
            if (calculateScore == JOptionPane.NO_OPTION) {
                int result = 0;
                for (Component b : handpanel.getComponents()) {
                    String name = b.getName();
                    if (name.contains("black")) result += 50;
                    else if (name.contains("reverse") || name.contains("stop") ||
                            name.contains("two")) result += 20;
                    else result += Integer.parseInt(name.replaceAll("\\D+",""));

                }
                JOptionPane.showMessageDialog(null, "Your score is: "+
                        result);
            }
        });
        //TODO change rules based on game mode: whose turn it is now; or swap hands

        JMenuItem regular = new JMenuItem("Regular");
        regular.addActionListener(e -> {
            JOptionPane.showMessageDialog(null,
                    "Play regular UNO: no stacking of +2/+4 cards allowed");
            setTitle("UNO - Regular mode");
        });

        JMenuItem stack = new JMenuItem("Переводной");
        stack.addActionListener(e -> {
            JOptionPane.showMessageDialog(null,
                    "Play stack UNO: next player can stack a +4 (+2) card on a previous +4 " +
                            "(+2) card, progressing the penalty to the player after this one" +
                            ".\n" +
                            "Stacking a +4 card on +2 or vice versa is not allowed");
            setTitle("UNO - Stack mode");
        });

        JMenuItem seven = new JMenuItem("Seven-0");
        seven.addActionListener(e -> {
            JOptionPane.showMessageDialog(null,
                    "Play Seven-0 UNO: the person who played the 7 card is able " +
                            "to switch all of their cards with another player;\n " +
                            "the player who played the 0 card is able to make every player " +
                            "exchange all their cards to the next player");
            setTitle("UNO - Seven-0 mode");
        });

        JMenuItem jumpin = new JMenuItem("Jump-In");
        jumpin.addActionListener(e -> {
            JOptionPane.showMessageDialog(null,
                    "Play Jump-in UNO: if a player has exactly the same card (both number and" +
                            " color) as the top card of the discard pile,\n" +
                            " they may play it immediately, even if it is not their turn. \n" +
                            "The game then continues as if that player had just taken their turn");
            setTitle("UNO - Jump-In mode");
        });

        JButton uno = new JButton("UNO!!");
        uno.addActionListener(e -> {
            Game.serverInfos.add(new Info(1,
                    Deck.getCard(played.getName()),"uno"));
            //TODO make message show on all computers, include what computer has uno
            JOptionPane.showMessageDialog(null, "Player said UNO");
        });

        JButton challenge = new JButton("Someone didn't say UNO!");
        challenge.addActionListener(e -> {
            Game.serverInfos.add(new Info(handpanel.getComponents().length,
                    Deck.getCard(played.getName()), "challenge"));
            //TODO send message to previous player saying they have to take more cards
            JOptionPane.showMessageDialog(null, "Someone didn't say UNO!!");
        });
        menu.add(rules);
        menu.add(scores);
        gamemode.add(regular);
        gamemode.add(stack);
        gamemode.add(seven);
        gamemode.add(jumpin);

        bar.add(menu);
        bar.add(gamemode);
        bar.add(uno);
        bar.add(challenge);
        setJMenuBar(bar);
    }

    void setPiles(int client) {
        Deck.getDeck();
        JPanel center = new JPanel();
        pile = new JButton();
        pile.setPreferredSize(new Dimension(78,122));
        pile.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK));
        pile.setIcon(Deck.back);
        pile.setOpaque(true);
        pile.addActionListener(e -> {
            Card newcard = Game.cardQueue.remove();
            JButton ncard = new JButton(newcard.image);
            ncard.setName(newcard.name);
            //ncard.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK));
            ncard.addActionListener(go);
            handpanel.add(ncard,0);
            numcards.setText("Cards left: "+ handpanel.getComponents().length);
            if (Game.cardQueue.isEmpty()) {
                Card last = playedCards.get(playedCards.size()-1);
                Collections.shuffle(playedCards);
                Game.cardQueue = new LinkedBlockingQueue<>(playedCards);
                playedCards.clear();
                playedCards.add(last);
            }
        });

        center.add(pile);
        played = new JButton();
        played.setPreferredSize(new Dimension(78,122));
        played.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK));
        played.setOpaque(true);
        center.add(new JLabel("    "));
        center.add(played);
        if (client == 0) {
            Card start = Game.cardQueue.remove();
            played.setIcon(start.image);
            played.setName(start.name);
            Game.serverInfos.add(new Info(Game.cardQueue, start));
            color = start.name.substring(0, 3);
            if (color.equals("bla")) {
                //TODO make this dependent on whose turn it is now
                String[] cols = {"Green", "Blue", "Yellow", "Red"};
                int inp = JOptionPane.showOptionDialog(null, "Select the colour you want",
                        "Choose colour", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, cols, null);
                switch (inp) {
                    case 3:
                        JOptionPane.showMessageDialog(null, "Red");
                        color = "red";
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(null, "Yellow");
                        color = "yel";
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "Blue");
                        color = "blu";
                        break;
                    case 0:
                        JOptionPane.showMessageDialog(null, "Green");
                        color = "gre";
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Wrong number");
                }
            }
            playedCards.add(start);
        } else {
          //  played.setIcon(Game.serverInfos.get(Game.serverInfos.size()-1).getPlayed().image);
            color = color;
        }
        constraints.anchor = GridBagConstraints.CENTER;
        add(center);
    }

    void setHand() {
        Card[] cards = Game.dealCards();

        for (int i = 0; i < 7; i++) {
            JButton b = new JButton(cards[i].image);
            b.addActionListener(go);
          //  b.setBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.BLACK));
            b.setName(cards[i].name);
            handpanel.add(b);
        }
        JScrollPane mycards = new JScrollPane(handpanel);
        //mycards.setPreferredSize(new Dimension(650, 170));
        constraints.gridx = 0;
        constraints.gridy = 10;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.SOUTH;
        constraints.ipadx = 800;
        constraints.ipady = 170;

        add(mycards, constraints);
        numcards.setText("Cards left: "+ handpanel.getComponents().length);
    }

    void setTopPart() {


        numcards = new JLabel();
        //constraints.gridx = 20;
        //constraints.gridy = 0;
        constraints.ipadx = 50;
        constraints.anchor = GridBagConstraints.NORTHEAST;
        add(numcards, constraints);


    }

    private void newGame() {
        GameInterface nG = new Server();
        nG.setVisible(true);
        setVisible(false);
    }

    private void closeWindow() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public static void main(String[] args) {
    }
}
