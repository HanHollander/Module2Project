package client.controller;

import client.view.Printer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * The startup class for a new game.
 * @author Han Hollander
 */
public class Qwirkle { 
  
  private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static String fileName;
  private static String playerType;
  private static String strategyType;

  /**
   * Main method to run the game.
   * @param args args
   */
  public static synchronized void main(String[] args) {
    //Create a log name
    DateFormat df = new SimpleDateFormat("ddMMyyHHmmss");
    Date dateobj = new Date();
    fileName = df.format(dateobj);
    
    String name = "";
    String addr = "";
    String portString = "";
    
    
    if (args.length == 0) {
      //Get the player input.
      Printer.print("Welcome in Qwirkle");
      Printer.print("(only characters a-z A-Z and 1 to 16 characters long)");
      Printer.print("What is your name? \n");
      boolean validName = false;
      //Check for valid name
      while (!validName) {
        name = readInput();
        validName = isValidName(name);
        if (!validName) {
          Printer.print("Name not valid, please try again." + "\n");
        }
      }
      Printer.print("\nServer IP-adress: \n");
      addr = readInput();
      Printer.print("\nServer port: \n");
      portString = readInput();
      Printer.print("\nBot: 'b', Human: 'h'\n");
      boolean validPlayerType = false;
      //Check for valid type
      while (!validPlayerType) {
        playerType = readInput();
        if (playerType.equals("b") || playerType.equals("h")) {
          validPlayerType = true;
        }
        if (!validPlayerType) {
          Printer.print("Player type not valid, please try again." + "\n");
        }
      }
    //If arguments are given at startup.
    } else if (args.length == 4) {
      name = args[0];
      addr = args[1];
      portString = args[2];
      playerType = args[3];
    }
    
    if (playerType.equals("b")) {
      Printer.print("\nNaive: 'n', Smart: 's'");
      boolean validStratType = false;
      //Check for valid type
      while (!validStratType) {
        strategyType = readInput();
        if (strategyType.equals("n") || strategyType.equals("s")) {
          validStratType = true;
        }
        if (!validStratType) {
          Printer.print("Strategy type not valid, please try again." + "\n");
        }
      }
    }
   
    InetAddress host = null;
    int port = 0;
     
    //Check host.
    Printer.print("\nChecking host... ");
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
    new Game(name, host, port);
  }
  
  /**
   * Read input from System.in.
   * @return The input.
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
   * Checks if name is valid.
   * @param text The text
   * @return If the name is valid.
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
  
  public static String getPlayerType() {
    return playerType;
  }
  
  public static String getStrategyType() {
    return strategyType;
  }
}
