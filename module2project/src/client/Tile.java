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

}
