package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
  
  private Server server;
  private BufferedReader in;
  private BufferedWriter out;
  private String clientName;
  
  /**
   * Constructor for clienthandler.
   * @param serverArg server
   * @param sockArg socket
   * @throws IOException if not able to create in or out
   */
  public ClientHandler(Server serverArg, Socket sockArg) throws IOException {
    server = serverArg;
    in = new BufferedReader(new InputStreamReader(sockArg.getInputStream()));
    out = new BufferedWriter(new OutputStreamWriter(sockArg.getOutputStream()));
  }
  
  /**
   * CLIENT -> server.
   */
  public void run() {
    String text = "";
    try {
      while (text != null) {
        
        text = in.readLine();
        if (!(text == null) && !text.equals("\n")) {
          server.broadcast(clientName + ": " + text);
        }
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
    server.removeHandler(this);
    server.broadcast("[" + clientName + " has left]");
  }

}
