package client;

public class Tile {
  
  private String color;
  private String shape;
  
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";   //R
  public static final String ANSI_GREEN = "\u001B[32m"; //G
  public static final String ANSI_YELLOW = "\u001B[33m";  //Y
  public static final String ANSI_BLUE = "\u001B[34m"; //B
  public static final String ANSI_PURPLE = "\u001B[35m"; //P
  public static final String ANSI_CYAN = "\u001B[36m"; //O
  
  public Tile() {
    color = ".";
    shape = " ";
  }
  
  public Tile(String color, String shape) {
    this.color = color;
    this.shape = shape;
  }
  
  public String getColor() {
    return color;
  }
  
  public String getShape() {
    return shape;
  }
  
  public String toString() {
    return color + shape;
  }
  
  /**
   * Custom equals function to make life easier.
   * @param tile The tile to compare this to.
   * @return If the tiles match.
   */
  public boolean equals(Tile tile) {
    boolean equals = false;
    if (color.equals(tile.getColor()) && shape.equals(tile.getShape())) {
      equals = true;
    }
    return equals;
  }
  // o d s c x *
  public String colourToString() {
    String letter = "";
    if (shape.equals("o")) { letter = "A"; }
    if (shape.equals("d")) { letter = "B"; }
    if (shape.equals("s")) { letter = "C"; }
    if (shape.equals("c")) { letter = "D"; }
    if (shape.equals("x")) { letter = "E"; }
    if (shape.equals("*")) { letter = "F"; }
    String result = "";
    if (color.equals("R")) {result = ANSI_RED + letter + ANSI_RESET;}
    if (color.equals("G")) {result = ANSI_GREEN + letter + ANSI_RESET;}
    if (color.equals("B")) {result = ANSI_BLUE + letter + ANSI_RESET;}
    if (color.equals("P")) {result = ANSI_PURPLE + letter + ANSI_RESET;}
    if (color.equals("Y")) {result = ANSI_CYAN + letter + ANSI_RESET;}
    if (color.equals("O")) {result = ANSI_YELLOW + letter + ANSI_RESET;}
    
    return result;
  }
  
  
  
  
  
  
  
  
  
  
  
  
  

}
