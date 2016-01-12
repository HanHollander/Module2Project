package client;

import java.util.Map;

public class NaiveStrategy implements Strategy {

  public Move determineMove(Board board) {
    Tile tile = new Tile("R", "o");
    return new Move(tile, 40, 40);
  }

}
