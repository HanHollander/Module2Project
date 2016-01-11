package client;

import java.util.ArrayList;
import java.util.List;

public class Board {
  
  private ArrayList<ArrayList<Tile>> boardMatrix;
  
  /**
   * Board constructor. Constructs a empty board.
   */
  public Board() {
    boardMatrix = new ArrayList<ArrayList<Tile>>();
    for (int row = 0; row < 183; row++) {
      boardMatrix.add(new ArrayList<Tile>());
      for (int column = 0; column < 183; column++) {
        boardMatrix.get(row).add(new Tile());
      }
    }
  }
  
  /**
   * Executes the given move on the board.
   * @param move The move you want to apply to the board.
   */
  public void put(Move move) {
    int row = move.getRow();
    int column = move.getColumn();
    boardMatrix.get(row).set(column, move.getTile());
  }
  
  public Tile get(int row, int column) {
    return boardMatrix.get(row).get(column);
  }
  
  /**
   * Calculates the part of the board where there are tiles.
   * @return A list with 2 tupels, in the first: the min and 
   *         max row marge, in the second: the min and max 
   *         column marge.
   */
  public ArrayList<ArrayList<Integer>> getMarges() {
    ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
    result.add(new ArrayList<Integer>());
    result.add(new ArrayList<Integer>());
    
    int row = 0;
    int column = 0;
    int rowMin = 0;
    int rowMax = 0;
    int columnMin = 0;
    int columnMax = 0;
    String empty = ". ";
    
    //Get the start row
    while (row < 182 && boardMatrix.get(row).get(column).toString().equals(empty)) { 
      if (column < 182) {
        column++;
      } else {
        column = 0;
        row++;
      }
    }
    
    rowMin = row - 1;
    
    row = 182;
    column = 0;
    //Get the end row
    while (row > 0 && boardMatrix.get(row).get(column).toString().equals(empty)) { 
      if (column > 0) {
        column--;
      } else {
        column = 182;
        row--;
      }
    }
    rowMax = row + 1;
    
    if (rowMin < rowMax) {
      row = 0;
      column = 0;
      //Get the start column
      while (column < 182 && boardMatrix.get(row).get(column).toString().equals(empty)) { 
        if (row < 182) {
          row++;
        } else {
          row = 0;
          column++;
        }
      }
      columnMin = column - 1;
      
      row = 0;
      column = 182;
      //Get the end row
      while (column > 0 && boardMatrix.get(row).get(column).toString().equals(empty)) { 
        if (row > 0) {
          row--;
        } else {
          row = 182;
          column--;
        }
      }
      columnMax = column + 1;
    } else {
      result.get(0).add(90);
      result.get(0).add(92);
      result.get(1).add(90);
      result.get(1).add(92);
      System.out.println("DINKIE");
    }
    result.get(0).add(rowMin);
    result.get(0).add(rowMax);
    result.get(1).add(columnMin);
    result.get(1).add(columnMax);

    
    return result;
  }
  
  /**
   * Creates a new board and copies the contents of the current board to the new board.
   * @return A copy of board.
   */
  public Board deepCopy() {
    Board result = new Board();
    for (int row = 0; row < 183; row++) {
      for (int column = 0; column < 183; column++) {
        result.boardMatrix.get(row).set(column, this.boardMatrix.get(row).get(column));
      }
    }
    return result;
  }
  
  /**
   * Returns a string representation of the board.
   */
  public String toString() {
    String result = "";
    ArrayList<ArrayList<Integer>> marges = getMarges();
    int rowMin = marges.get(0).get(0);
    int rowMax = marges.get(0).get(1);
    int columnMin = marges.get(1).get(0);
    int columnMax = marges.get(1).get(1);
    result = "XX ";
    for (int column = columnMin; column <= columnMax; column++) {
      if ((column - columnMin) > 9) {
        result = result + (column - columnMin) + " ";
      } else {
        result = result + (column - columnMin) + "  ";
      }
    }
    result = result + "\n";
    for (int row = rowMin; row <= rowMax; row++) {
      if ((row - rowMin) > 9) {
        result = result + (row - rowMin) + " ";
      } else {
        result = result + (row - rowMin) + "  ";
      }
      for (int column = columnMin; column <= columnMax; column++) {
        result = result + boardMatrix.get(row).get(column).toString() + " ";
      }
      result = result + "\n";
    }
    return result;
  }
  
  public Boolean checkMove(Move move) {
    
    return true;
  }
  
  
  
  /**
   * Main method. Purpose = testing.
   * @param args Arguments
   */
  public static void main(String[] args) {
    Board board = new Board();
    Tile tile = new Tile("A", "B");
    Move move = new Move(tile, 91, 91);
    Move move2 = new Move(tile, 75, 91);
    Move move3 = new Move(tile, 91, 80);
    board.put(move);
    board.put(move2);
    board.put(move3);
    System.out.print(board.toString());
  }
  
}
