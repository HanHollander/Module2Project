package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NaiveStrategy implements Strategy {
  
  public static final int DIM = 183;

  /**
   * Determines a valid move. Very, very bad AI.
   * @param board the board
   * @param hand the hand
   */
  public Move determineMove(Board board, List<Tile> hand) {
    Move result = null;
    List<List<Integer>> places = getPossiblePlaces(board);
    System.out.println(hand);
    for (Tile tile : hand) {
      for (List<Integer> place : places) {
        Move newMove = new Move(tile, place.get(0), place.get(1));
        if (board.checkMove(newMove)) {
          result = newMove;
        }
      }
    }
    if (result == null) {
      result = new Move(hand.get(1));
    }
    return result;
  }
  
  /**
   * Gets places where a tile can be.
   * @param board the board
   * @return list of places
   */
  public List<List<Integer>> getPossiblePlaces(Board board) {
    List<List<Integer>> result = new ArrayList<>();
    for (int i = 0; i < DIM; i++) {
      for (int j = 0; j < DIM; j ++) {
        if (!board.getTile(i, j).equals(new Tile(".", " "))) {
          if (board.getTile(i - 1, j).equals(new Tile(".", " "))) {
            List<Integer> add = new ArrayList<>();
            add.add(i - 1);
            add.add(j);
            result.add(add);
          } 
          if (board.getTile(i + 1, j).equals(new Tile(".", " "))) {
            List<Integer> add = new ArrayList<>();
            add.add(i + 1);
            add.add(j);
            result.add(add);
          }
          if (board.getTile(i, j - 1).equals(new Tile(".", " "))) {
            List<Integer> add = new ArrayList<>();
            add.add(i);
            add.add(j - 1);
            result.add(add);
          }
          if (board.getTile(i, j + 1).equals(new Tile(".", " "))) {
            List<Integer> add = new ArrayList<>();
            add.add(i);
            add.add(j + 1);
            result.add(add);
          }
        }
      }
    }
    return result;
    
  }

}
