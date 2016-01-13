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

    try {
      host = InetAddress.getByName(addr);
    } catch (UnknownHostException e) {
      System.out.println("ERROR: no valid hostname!");
      System.exit(0);
    }

    try {
      port = Integer.parseInt(portString);
    } catch (NumberFormatException e) {
      System.out.println("ERROR: no valid portnummer!");
      System.exit(0);
    }
    
    Socket sock;
    BufferedReader in = null;
    BufferedWriter out = null;
    try {
      sock = new Socket(host, port);
      in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
      out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
    } catch (IOException e) {
      System.out.println("Could not create socket.");
      System.exit(0);
    }
    
    writeSocketOutput(out, "HELLO " + name);
    String welcomeMessage = readSocketInput(in); //////////////////WHERE WE STOPPED\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
  }
  
  public static String readInput() {
    String input = "";
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
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
      System.out.println("Could not send message to server.");
    }
  }
  
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
