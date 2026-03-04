import java.io.*;
import java.net.*;

public class ClientC {
    public static void main(String[] args) {
        String serverName = "localhost"; // Server IP or hostname
        int serverPort = 3000;           // Server port number
        String clientID = "C";           // Client identifier

        try (
            Socket clientSocket = new Socket(serverName, serverPort);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Client " + clientID + " connected to server.");

            String userCommand;
            while (true) {
                System.out.print("Enter command: ");
                userCommand = stdIn.readLine();

                if (userCommand != null && !userCommand.isEmpty()) {
                    String formattedCommand = clientID + " " + userCommand;
                    out.println(formattedCommand); 

                    String serverResponse = in.readLine(); 
                    System.out.println("Server response: " + serverResponse);
                }
            }
        } catch (IOException e) {
            System.err.println("I/O error in client: " + e.getMessage());
        }
    }
}