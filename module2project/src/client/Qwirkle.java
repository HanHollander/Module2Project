package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Qwirkle {
  
  private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Game game;
  private static String fileName;

  /**
   * Main method to run the game.
   * @param args args
   */
  public static synchronized void main(String[] args) {
    //Create a log name
    DateFormat df = new SimpleDateFormat("ddMMyyHHmmss");
    Date dateobj = new Date();
    fileName = df.format(dateobj);
    
    //Get the player input.
    Printer.print("Welcome in Qwirkle");
    Printer.print("(only characters a-z A-Z and 1 to 16 characters long)");
    Printer.print("What is your name? \n");
    boolean validName = false;
    String name = "";
    //Check for valid name
    while (!validName) {
      name = readInput();
      validName = isValidName(name);
      if (!validName) {
        Printer.print("Name not valid, please try again." + "\n");
      }
    }
    Printer.print("Server IP-adress: \n");
    String addr = readInput();
    Printer.print("Server port: \n");
    String portString = readInput();
    Printer.print("Bot: 'b', Human: 'h'  \n");
    String playerType = readInput();
    Printer.print("\n");
    
    InetAddress host = null;
    int port = 0;
    
    //Check host.
    Printer.print("Checking host... ");
    try {
      host = InetAddress.getByName(addr);
      Printer.print("Host accepted.");
    } catch (UnknownHostException e) {
      Printer.print("This is not a valid hostname!");
      System.exit(0);
    }
    
    //Check port.
    Printer.print("Checking port... ");
    try {
      port = Integer.parseInt(portString);
      Printer.print("Port accepted.");
    } catch (NumberFormatException e) {
      Printer.print("This is not a valid portnummer!");
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
        Printer.print("Could not read line.");
      }
      validInput = true;
    }
    return input;
  }
  
  /**
   * checks if name is valid
   * @param text text
   * @return if valid
   */
  private static boolean isValidName(String text) {
    List<String> allowedChars = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 
        "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", 
        "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", 
        "S", "T", "U", "V", "W", "X", "Y", "Z");
    boolean result = true;
    String name = text;
    result = result && name.length() < 17;
    for (int i = 0; i < name.length(); i++) {
      result = result && allowedChars.contains(name.substring(i, i + 1));
    }
    return result;
  }

  public static String getFileName() {
    return fileName;
  }

  public static void setFileName(String fileName) {
    Qwirkle.fileName = fileName;
  }
}
