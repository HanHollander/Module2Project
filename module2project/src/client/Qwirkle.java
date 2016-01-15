package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Qwirkle {
  
  private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Game game;

  /**
   * Main method to run the game.
   * @param args args
   */
  public static synchronized void main(String[] args) {
    //Get the player input.
    System.out.println("Welcome in Qwirkle");
    System.out.println("(only characters a-z A-Z and 1 to 16 characters long)");
    System.out.println("What is your name?");
    String name = readInput();
    System.out.println("");
    System.out.println("Server IP-adress:");
    String addr = readInput();
    System.out.println("");
    System.out.println("Server port:");
    String portString = readInput();
    System.out.println("");
    System.out.println("Bot: 'b', Human: 'h'");
    String playerType = readInput();
    
    InetAddress host = null;
    int port = 0;
    
    //Check host.
    System.out.println("Checking host... ");
    try {
      host = InetAddress.getByName(addr);
      System.out.println("Host accepted.");
    } catch (UnknownHostException e) {
      System.out.println("This is not a valid hostname!");
      System.exit(0);
    }
    
    //Check port.
    System.out.println("Checking port... ");
    try {
      port = Integer.parseInt(portString);
      System.out.println("Port accepted.");
    } catch (NumberFormatException e) {
      System.out.println("This is not a valid portnummer!");
      System.exit(0);
    }
    
    //Start a new game.
    game = new Game(name, host, port, playerType);
  }
  
  /**
   * Read input from System.in.
   * @return the input.
   */
  public static String readInput() {
    String input = "";
    boolean validInput = false;
    while (!validInput) {
      try {
        input = reader.readLine();
      } catch (IOException e) {
        System.out.println("Could not read line.");
      }
      validInput = true;
    }
    return input;
  }
}
