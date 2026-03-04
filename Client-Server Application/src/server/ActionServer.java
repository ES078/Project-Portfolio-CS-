import java.io.*;
import java.net.*;

public class ActionServer {
    private static final int PORT = 3000;

    public static void main(String[] args) {
        SharedActionState sharedState = new SharedActionState();
        System.out.println("Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");
                new Thread(new ClientHandler(clientSocket, sharedState)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;
    private final SharedActionState sharedState;

    public ClientHandler(Socket socket, SharedActionState sharedState) {
        this.socket = socket;
        this.sharedState = sharedState;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String command;
            while ((command = in.readLine()) != null) {
                
                String[] parts = command.split(" ", 2);
                if (parts.length < 2) {
                    out.println("Invalid command format");
                    continue;
                }

                String clientID = parts[0];
                String userCommand = parts[1];
                
                System.out.println("Received: " + clientID + " " + userCommand);

                String response = sharedState.processTransaction(clientID, userCommand);
                
                System.out.println("Processed: " + response);

                out.println(response); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}