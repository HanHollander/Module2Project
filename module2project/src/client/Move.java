package client;

public class Move {
  
  public enum Type {MOVE, SWAP};
  private Type type;
  private String colour;
  private String shape;
  private int row;
  private int column;
  
  /**
   * Constructor for MOVE move.
   * @param colour c of t
   * @param shape s of t
   * @param row r of t
   * @param column c of t
   */
  public Move(String colour, String shape, int row, int column) {
    this.type = Type.MOVE;
    this.colour = colour;
    this.shape = shape;
    this.row = row;
    this.column = column;
  }
  
  /**
   * Constructor for SWAP move.
   * @param colour colour of tile
   * @param shape shape of tile
   */
  public Move(String colour, String shape) {
    this.type = Type.SWAP;
    this.colour = colour;
    this.shape = shape;
  }

  public String getColour() {
    return colour;
  }

  public String getShape() {
    return shape;
  }


  public int getRow() {
    return row;
  }

  public int getColumn() {
    return column;
  }
  
  public Type getType() {
    return type;
  }


}
