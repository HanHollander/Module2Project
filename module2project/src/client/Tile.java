package client;

public class Tile {
  
  private String color;
  private String shape;
  
  public Tile() {
    color = ".";
    shape = " ";
  }
  
  public Tile(String color, String shape) {
    this.color = color;
    this.shape = shape;
  }
  
  public String toString() {
    return color + shape;
  }

}
