package server;

public class Move {
  
  public enum Type {MOVE, SWAP, END, ANY};
  private Type type;
  private Tile tile;
  private int row;
  private int column;
  
  /**
   * Constructor for MOVE move.
   * @param colour c of t
   * @param shape s of t
   * @param row r of t
   * @param column c of t
   */
  public Move(Tile tile, int row, int column) {
    this.type = Type.MOVE;
    this.tile = tile;
    this.row = row;
    this.column = column;
  }
  
  /**
   * Constructor for SWAP move.
   * @param colour colour of tile
   * @param shape shape of tile
   */
  public Move(Tile tile) {
    this.type = Type.SWAP;
    this.tile = tile;
  }
  
  public Move(Type type) {
    this.type = type;
  }

  public Tile getTile() {
    return tile;
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
  
  public String toString() {
    return getTile().toString() + " " + getRow() + " " + getColumn();
  }


}
