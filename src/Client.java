import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client extends GameInterface{

    static Queue queue;
    public Client() {
        super(1);
        setTitle("UNO - client");
    }



    public static void main(String[] args) {
        Client g = new Client();
        g.setVisible(true);
        String hostName = "localhost";
        int portNumber = 4444;

        try (
                Socket s = new Socket(hostName, portNumber);
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                ) {
            Object fromServer, fromUser;



            while ((fromServer = in.readObject()) != null) {
                System.out.println("Server: " + fromServer.toString());

                    played.setIcon(((Info) fromServer).getPlayed().image);
                    Game.cardQueue = ((Info) fromServer).getPile();
                if (Protocol.state == Protocol.BEGINNING) {
                    Card[] cs = Game.dealCards();
                    int i = 0;

                    for (Component b : handpanel.getComponents()) {

                        ((JButton) b).setIcon(cs[i].image);
                        System.out.println(i);
                        i++;
                    }
                    System.out.println(Game.cardQueue.size());
                    //fromUser = GameInterface.serverInfos.get(GameInterface.serverInfos.size()-1);

                }

                fromUser = "image set";
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser.toString());
                    out.writeObject(fromUser);
                }
            }


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}