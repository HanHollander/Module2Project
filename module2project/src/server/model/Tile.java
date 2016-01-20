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
  
  public String getColor() {
    return color;
  }
  
  public String getShape() {
    return shape;
  }
  
  public String toString() {
    return color + shape;
  }
  
  public boolean equals(Tile tile) {
    return (color.equals(tile.getColor()) && shape.equals(tile.getShape()));
  }

}
