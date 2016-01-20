package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SmartStrategy implements Strategy {
  
  public static final int DIM = 183;

  private List<Move> thePerfectTurn;
  
  public SmartStrategy() {
    thePerfectTurn = new ArrayList<>();
  }
  
  public Move determineMove(Board board, List<Tile> hand, ComputerPlayer player) {
    Move theMove;
    if (thePerfectTurn.size() == 0) {
      List<List<Integer>> possiblePlaces = getPossiblePlaces(board);
      HashMap<List<Move>, Integer> result = new HashMap<List<Move>, Integer>();
      for (List<Integer> coord : possiblePlaces) {
        for (Tile tile : hand) {
          int row = coord.get(0);
          int col = coord.get(1);
          Move move = new Move(tile, row, col);
          if (board.checkMove(move)) {
            List<Tile> testHand = new ArrayList<>();
            List<Move> testTurn = new ArrayList<>();
            testTurn.add(move);
            testHand.addAll(hand);
            testHand.remove(tile);
            Board testBoard = board.deepCopy();
            testBoard.putTile(move);
            HashMap<List<Move>, Integer> tussenResult = new HashMap<List<Move>, Integer>();
            tussenResult = getBestTurn(testBoard, hand, testTurn);
            Set<List<Move>> tussenResultKeySet = tussenResult.keySet();
            for (List<Move> tussenTurn : tussenResultKeySet) {
              result.put(tussenTurn, tussenResult.get(tussenTurn));
            }
          }
        }
      }
      Set<List<Move>> resultKeySet = result.keySet();
      List<Move> bestTurn = new ArrayList<Move>();;
      int bestScore = -1;
      for (List<Move> selectedTurn : resultKeySet) {
        int turnScore = result.get(selectedTurn);
        if (turnScore > bestScore) {
          bestTurn = new ArrayList<Move>();
          bestTurn.addAll(selectedTurn);
          bestScore = turnScore;
        }
      }
      thePerfectTurn = new ArrayList<Move>();
      thePerfectTurn.addAll(bestTurn);
      theMove = thePerfectTurn.get(0);
      thePerfectTurn.remove(0);
      if (thePerfectTurn.size() == 0) { 
        player.setMadeMove(true);
      }
    } else {
      theMove = thePerfectTurn.get(0);
      thePerfectTurn.remove(0);
      if (thePerfectTurn.size() == 0) { 
        player.setMadeMove(true);
      }
    }
    return theMove;
  }
  
  /**
   * Gets places where a tile can be.
   * @param board the board
   * @return list of places
   */
  public List<List<Integer>> getPossiblePlaces(Board board) {
    List<List<Integer>> result = new ArrayList<>();
    Tile empty = new Tile(".", " ");
    for (int i = 0; i < DIM; i++) {
      for (int j = 0; j < DIM; j ++) {
        if (!board.getTile(i, j).equals(empty)) {
          if (board.getTile(i - 1, j).equals(empty)) {
            List<Integer> add = new ArrayList<>();
            add.add(i - 1);
            add.add(j);
            result.add(add);
          } 
          if (board.getTile(i + 1, j).equals(empty)) {
            List<Integer> add = new ArrayList<>();
            add.add(i + 1);
            add.add(j);
            result.add(add);
          }
          if (board.getTile(i, j - 1).equals(empty)) {
            List<Integer> add = new ArrayList<>();
            add.add(i);
            add.add(j - 1);
            result.add(add);
          }
          if (board.getTile(i, j + 1).equals(empty)) {
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
  
  public HashMap<List<Move>, Integer> getBestTurn(Board board, List<Tile> hand, List<Move> turn) {
    Tile empty = new Tile(".", " ");
    Move move = turn.get(turn.size() - 1);
    int row = move.getRow();
    int col = move.getColumn();
    HashMap<List<Move>, Integer> result = new HashMap<List<Move>, Integer>();
    if (board.getTile(row + 1, col).equals(empty)) {
      for (Tile tile : hand) {
        Move testMove = new Move(tile, row + 1, col);
        if (board.checkMove(testMove)) {
          List<Tile> testHand = new ArrayList<>();
          List<Move> testTurn = new ArrayList<>();
          testTurn.addAll(turn);
          testTurn.add(testMove);
          testHand.addAll(hand);
          testHand.remove(tile);
          Board testBoard = board.deepCopy();
          testBoard.putTile(testMove);
          HashMap<List<Move>, Integer> tussenResult = new HashMap<List<Move>, Integer>();
          tussenResult = getBestTurn(testBoard, hand, testTurn);
          Set<List<Move>> tussenResultKeySet = tussenResult.keySet();
          for (List<Move> tussenTurn : tussenResultKeySet) {
            int testScoreDinkie = tussenResult.get(tussenTurn);
            result.put(tussenTurn, testScoreDinkie);
          }
        }
      }
    }
    if (board.getTile(row - 1, col).equals(empty)) {
      for (Tile tile : hand) {
        Move testMove = new Move(tile, row - 1, col);
        if (board.checkMove(testMove)) {
          List<Tile> testHand = new ArrayList<>();
          List<Move> testTurn = new ArrayList<>();
          testTurn.addAll(turn);
          testTurn.add(testMove);
          testHand.addAll(hand);
          testHand.remove(tile);
          Board testBoard = board.deepCopy();
          testBoard.putTile(testMove);
          HashMap<List<Move>, Integer> tussenResult = new HashMap<List<Move>, Integer>();
          tussenResult = getBestTurn(testBoard, hand, testTurn);
          Set<List<Move>> tussenResultKeySet = tussenResult.keySet();
          for (List<Move> tussenTurn : tussenResultKeySet) {
            int testScoreDinkie = tussenResult.get(tussenTurn);
            result.put(tussenTurn, testScoreDinkie);
          }
        }
      }
    }
    if (board.getTile(row, col + 1).equals(empty)) {
      for (Tile tile : hand) {
        Move testMove = new Move(tile, row, col + 1);
        if (board.checkMove(testMove)) {
          List<Tile> testHand = new ArrayList<>();
          List<Move> testTurn = new ArrayList<>();
          testTurn.addAll(turn);
          testTurn.add(testMove);
          testHand.addAll(hand);
          testHand.remove(tile);
          Board testBoard = board.deepCopy();
          testBoard.putTile(testMove);
          HashMap<List<Move>, Integer> tussenResult = new HashMap<List<Move>, Integer>();
          tussenResult = getBestTurn(testBoard, hand, testTurn);
          Set<List<Move>> tussenResultKeySet = tussenResult.keySet();
          for (List<Move> tussenTurn : tussenResultKeySet) {
            int testScoreDinkie = tussenResult.get(tussenTurn);
            result.put(tussenTurn, testScoreDinkie);
          }
        }
      }
    }
    if (board.getTile(row, col - 1).equals(empty)) {
      for (Tile tile : hand) {
        Move testMove = new Move(tile, row, col - 1);
        if (board.checkMove(testMove)) {
          List<Tile> testHand = new ArrayList<>();
          List<Move> testTurn = new ArrayList<>();
          testTurn.addAll(turn);
          testTurn.add(testMove);
          testHand.addAll(hand);
          testHand.remove(tile);
          Board testBoard = board.deepCopy();
          testBoard.putTile(testMove);
          HashMap<List<Move>, Integer> tussenResult = new HashMap<List<Move>, Integer>();
          tussenResult = getBestTurn(testBoard, hand, testTurn);
          Set<List<Move>> tussenResultKeySet = tussenResult.keySet();
          for (List<Move> tussenTurn : tussenResultKeySet) {
            int testScoreDinkie = tussenResult.get(tussenTurn);
            result.put(tussenTurn, testScoreDinkie);
          }
        }
      }
    }
    if (result.size() == 0) {
      result.put(turn, board.getScoreCurrentTurn());
    } else {
      Set<List<Move>> resultKeySet = result.keySet();
      List<Move> bestTurn = new ArrayList<Move>();;
      int bestScore = -1;
      for (List<Move> selectedTurn : resultKeySet) {
        int turnScore = result.get(selectedTurn);
        if ( turnScore > bestScore) {
          bestTurn = new ArrayList<Move>();
          bestTurn.addAll(selectedTurn);
          bestScore = turnScore;
        }
      }
      result = new HashMap<List<Move>, Integer>();
      result.put(bestTurn, bestScore);
    }
    return result;
  }
}
