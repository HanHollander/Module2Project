package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class Qwirkle {
  
  private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  
  
  public static void main(String[] args) {
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
    
    InetAddress host = null;
    int port = 0;
    
    System.out.print("Checking host... ");
    try {
      host = InetAddress.getByName(addr);
      System.out.println("Host accepted.");
    } catch (UnknownHostException e) {
      System.out.println("This is not a valid hostname!");
      System.exit(0);
    }
    
    System.out.print("Checking port... ");
    try {
      port = Integer.parseInt(portString);
      System.out.println("Port accepted.");
    } catch (NumberFormatException e) {
      System.out.println("This is not a valid portnummer!");
      System.exit(0);
    }
    
    Socket sock;
    BufferedReader in = null;
    BufferedWriter out = null;
    try {    
      System.out.print("Creating socket... ");
      sock = new Socket(host, port);
      System.out.println("Socket created.");
      System.out.print("Creating streams... ");
      in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
      out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
      System.out.println("Streams created.");
    } catch (IOException e) {
      System.out.println("Could not create socket and streams.");
      System.exit(0);
    }
    
    writeSocketOutput(out, Game.HELLO + " " + name);
    String welcomeMessage = readSocketInput(in); //////////////////WHERE WE STOPPED\\\\\\\\\\\\\\\\\\\\\\\\\\\
    System.out.println(welcomeMessage);
    
  }
  
  /////////////STILL VALIDATE INPUT!!!\\\\\\\\\\\\
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
  
  public static void writeSocketOutput(BufferedWriter out, String msg) {
    try {
      out.write(msg);
      out.newLine();
      out.flush();
    } catch (IOException e) {
      System.out.println("Could not send message to Server.");
    }
  }
  
  /////////////STILL VALIDATE INPUT!!!\\\\\\\\\\\\
  public static String readSocketInput(BufferedReader in) {
    String input = "";
    boolean validInput = false;
    while (!validInput) {
      try {
        input = in.readLine();
      } catch (IOException e) {
        System.out.println("Could not read line from server.");
      }
      validInput = true;
    }
    return input;
  }
}
