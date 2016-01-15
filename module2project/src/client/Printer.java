package client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Printer {
  
  public static final String PATH = "src\\client\\";
  public static BufferedWriter log;
  
  /**
   * A printer to make logs.
   */
  public Printer() {
    DateFormat df = new SimpleDateFormat("dd/MM/yy_HH:mm:ss");
    Date dateobj = new Date();
    String fileName = df.format(dateobj);
    File file = new File(PATH + fileName);
    try {
      log = new BufferedWriter(new FileWriter(file));
    } catch (IOException e) {
      System.out.println("Could not create writer.");
    }
    
  }

  /**
   * Prints to console and file.
   * @param text to be printed
   */
  public void print(String text) {
    System.out.println(text);
    try {
      log.write(text);
    } catch (IOException e) {
      System.out.println("Could not write.");
    }
  }
  
}
