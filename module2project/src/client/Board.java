package client;

import java.util.ArrayList;
import java.util.List;

public class Board {
  
  private List<List<Tile>> boardMatrix;
  
  /**
   * Board constructor. Constructs a empty board.
   */
  public Board() {
    for (int row = 0; row < 182; row++) {
      boardMatrix.add(new ArrayList<Tile>());
      for (int collumn = 0; collumn < 182; collumn++) {
        boardMatrix.get(row).add(new Tile());
      }
    }
  }
  
  /**
   * Calculates the part of the board where there are tiles
   * @return 
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
    while (boardMatrix.get(row).get(column).toString().equals(empty) && row != 182) { //Get the start row
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
    while (boardMatrix.get(row).get(column).toString().equals(empty) && row != 0) { //Get the end row
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
      while (boardMatrix.get(row).get(column).toString().equals(empty) && column != 182) { //Get the start column
        if (row < 182) {
          row++;
        } else {
          row = 0;
          column++;
        }
      }
      columnMax = column - 1;
      
      row = 0;
      column = 182;
      while (boardMatrix.get(row).get(column).toString().equals(empty) && column != 0) { //Get the end row
        if (row > 0) {
          row--;
        } else {
          row = 182;
          column--;
        }
      }
      columnMax = column + 1;
    } else {
      result.get(0).add(91);
      result.get(0).add(93);
      result.get(1).add(91);
      result.get(1).add(93);
    }
    result.get(0).add(rowMin);
    result.get(0).add(rowMax);
    result.get(1).add(columnMin);
    result.get(1).add(columnMax);

    
    return result;
  }
  
  public Boolean checkMove(Tile tile, int row, int collumn) {
    return true;
  }
  
}
