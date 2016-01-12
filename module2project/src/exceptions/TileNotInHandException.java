package exceptions;

import client.Tile;

public class TileNotInHandException extends Exception {
  
  public TileNotInHandException(Tile tile) {
    super("Tile [" + tile.getColor() + tile.getShape() + "] is not in your hand.");
  }

}
