package exceptions;

import client.Tile;

public class TileNotInHandException extends Exception {
  
  public TileNotInHandException(Tile tile) {
    super("Tile [" + tile.toString() + "] is not in your hand.");
  }

}
