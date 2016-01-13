package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class ClientHandler extends Thread {
  
  private Server server;
  private Socket socket;
  private BufferedReader in;
  private BufferedWriter out;
  private String clientName;
  private int playerNr;
  
  /**
   * Constructor for clienthandler.
   * @param serverArg server
   * @param sockArg socket
   * @throws IOException if not able to create in or out
   */
  public ClientHandler(int playerNr, Server serverArg, Socket sockArg) throws IOException {
    server = serverArg;
    socket = sockArg;
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    this.playerNr = playerNr;
  }
  
  /**
   * CLIENT -> server.
   */
  public void run() {
    String text = "";
    // Starting procedure
    try {
      text = in.readLine();
      if (isValidStartMessage(text)) {
        String[] textParts = text.split(" ");
        clientName = textParts[1];
        System.out.println("Received from client " + playerNr + ": " + text);
        sendMessage("WELCOME " + clientName + " " + playerNr);
      } else {
        // KICK
        shutdown();
      }
    } catch (IOException e) {
      shutdown();
    }
  }
  
  /**
   * SERVER -> client.
   * @param msg message
   */
  public void sendMessage(String msg) {
    try {
      out.write(msg);
      out.newLine();
      out.flush();
    } catch (IOException e) {
      shutdown();
    }
  }
  
  private void shutdown() {
    try {
      socket.close();
    } catch (IOException e) {
      System.out.println("Could not close socket in the shutdown procedure.");
    }
    server.removeHandler(this);
  }
  
  private Boolean isValidStartMessage(String text) {
    List<String> allowedChars = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 
        "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", 
        "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", 
        "S", "T", "U", "V", "W", "X", "Y", "Z");
    Boolean result;
    result = text.startsWith("HELLO ");
    if (!result) {
      String name = text.substring(6);
      result = result && name.length() < 17;
      for (int i = 0; i < name.length(); i++) {
        result = result && allowedChars.contains(name.substring(i, i));
      }
    }
    return result;
  }

}
