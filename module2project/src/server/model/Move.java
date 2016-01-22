package server.model;

public class Move {
  
  public enum Type { MOVE, SWAP, END, ANY; }
  
  private Type type;
  private Tile tile;
  private int row;
  private int column;
  
  /**
   * Constructor for MOVE move.
   * @param tile Tile
   * @param row Row on the board
   * @param column Column on the board
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
//    int result = 0 + row;
//    return result;
    return row;
  }

  public int getColumn() {
//    int result = 0 + column;
//    return result;
    return column;
  }
  
  public Type getType() {
    return type;
  }
  
  public String toString() {
    return getTile().toString() + " " + getRow() + " " + getColumn();
  }


}
