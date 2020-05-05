import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Client extends GameInterface{

    public Client() {
        super(1);
        setTitle("UNO - client");
    }



    public static void main(String[] args) {
        Client g = new Client();

        String hostName = "localhost";
        int portNumber = 4444;

        try (
                Socket s = new Socket(hostName, portNumber);
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream())
                ) {
            Object fromServer, fromUser;



            while ((fromServer = in.readObject()) != null) {
                System.out.println("Server: " + fromServer.toString());

                    played.setIcon(((Info) fromServer).getPlayed().image);
                    played.setName(((Info) fromServer).getPlayed().name);
                    GameInterface.color = played.getName().substring(0,3);
                    Game.cardQueue = ((Info) fromServer).getPile();
                if (Protocol.state == Protocol.BEGINNING) {
                    Card[] cs = Game.dealCards();
                    int i = 0;

                    for (Component b : handpanel.getComponents()) {

                        ((JButton) b).setIcon(cs[i].image);
                        b.setName(cs[i].name);
                        i++;
                    }
                    Protocol.state = Protocol.MOVE;
                }
                System.out.println(Game.cardQueue.size());
                fromUser = Game.serverInfos.get(Game.serverInfos.size()-1);
                g.setVisible(true);
               // fromUser = "image set";
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