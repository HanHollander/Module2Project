package server;

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
  
  public String getColor(){
    return color;
  }
  
  public String getShape(){
    return shape;
  }
  
  public String toString() {
    return color + shape;
  }

}
