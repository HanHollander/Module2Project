package server.model;

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
  
  /*@ pure*/ public String getColor() {
    return color;
  }
  
  /*@ pure*/ public String getShape() {
    return shape;
  }
  
  /*@ pure*/ public String toString() {
    return color + shape;
  }
  
  /*@ pure*/ public boolean equals(Tile tile) {
    return (color.equals(tile.getColor()) && shape.equals(tile.getShape()));
  }

}
