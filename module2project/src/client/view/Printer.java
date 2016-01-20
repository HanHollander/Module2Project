package client.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import client.controller.Game;
import client.controller.Qwirkle;
import client.model.HumanPlayer;

public class Printer {
  
  public static final String PATH = "E:" + File.separator + "LocalGit" 
      + File.separator + "Module2Project" + File.separator + "module2project" 
      + File.separator + "logs" + File.separator;
  public static final BufferedWriter logger = createWriter();
  public static File file;
 
  /**
   * Creates a static bufferedwriter.
   * @return a writer
   */
  public static BufferedWriter createWriter() {
    String fileName = Qwirkle.getFileName();
    file = new File(PATH + "c" + fileName + ".txt");
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        System.out.println("Could not create new file.");
      }
    }
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(file));
    } catch (IOException e) {
      System.out.println("Could not create writer.");
    }
    return writer;
  }
  
  /**
   * Prints to console and file.
   * @param text to be printed
   */
  public static void print(String text) {
    System.out.println(text);
    try {
      DateFormat df = new SimpleDateFormat("dd/MM/yy_HH:mm:ss");
      Date dateobj = new Date();
      String dateTime = df.format(dateobj);
      logger.write(dateTime + ": " + text);
      logger.newLine();
      logger.flush();
    } catch (IOException e) {
      System.out.println("Could not write.");
    }
  }
  
  /**
   * Prints an exception to console and file.
   * @param e to be printed
   */
  public static void print(Exception e) {
    System.out.println(e);
    try {
      DateFormat df = new SimpleDateFormat("dd/MM/yy_HH:mm:ss");
      Date dateobj = new Date();
      String dateTime = df.format(dateobj);
      logger.write(dateTime + ": " + e);
      logger.newLine();
      logger.flush();
    } catch (IOException ex) {
      System.out.println("Could not write.");
    }
  }
  
  public static void printBoard(Game game) {
    String board = "u\033[2J" + game.getBoard().toString() + "\nHand: " 
        + game.getPlayer().handToString() + "\n" + "Tiles in pool: " + game.getPool() + "\n";
    print(board);
    game.printScores();
    if (game.getPlayer() instanceof HumanPlayer && game.getPlayer().getHand().size() != 0) {
      Printer.print("\nHint: " 
          + game.getHintGen().getHint(game.getBoard(), game.getPlayer().getHand()).toString());
    }
  }
  
}