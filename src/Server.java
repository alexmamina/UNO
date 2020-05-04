import java.io.*;
import java.net.*;

public class Server extends GameInterface{

    public Server() {
        super(0);
       // super.setHand();
    }

    public static void main(String[] args) {
        Server g = new Server();
        g.setVisible(true);
        int portNumber = 4444;
        try {
            ServerSocket socket = new ServerSocket(portNumber);
            Socket clientSocket = socket.accept();
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

            Info inputobject, outputobject;


            //Initiate new conversation with client
            Protocol p = new Protocol();
            //Server starts talking so no input has been given yet.
            //processInput will return a greetings Object
            outputobject = p.processInput(Game.serverInfos.get(Game.serverInfos.size()-1));
            System.out.println(outputobject);
            out.writeObject(outputobject);

            System.out.println(Game.cardQueue.size());
            //Loop through all inputs
            while ((inputobject = (Info) in.readObject()) != null) {
                outputobject = p.processInput(inputobject);
                out.writeObject(outputobject);
                if (outputobject.equals("bye")) {
                    break;
                }
            }


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}