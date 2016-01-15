package client;

import java.util.ArrayList;
import java.util.List;

public class Board {
  
  private ArrayList<ArrayList<Tile>> boardMatrix;
  private ArrayList<Move> currentLocalTurn;
  
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
    currentLocalTurn = new ArrayList<Move>();
  }
  
  /**
   * Executes the given move on the board.
   * @param move The move you want to apply to the board.
   */
  public void putTile(Move move) {
    int row = move.getRow();
    int column = move.getColumn();
    boardMatrix.get(row).set(column, move.getTile());
    currentLocalTurn.add(move);
  }
  
  /**
   * Returns the Tile located at the given row and column.
   * @param row A row number of the board.
   * @param column A column number of the board.
   * @return the Tile located at the given row and column.
   */
  public Tile getTile(int row, int column) {
    return boardMatrix.get(row).get(column);
  }
  
  /**
   * Ends the current turn by removing all the moves from
   * the array of moves of the current (now previous) turn.
   */
  public void endTurn() {
    currentLocalTurn = new ArrayList<Move>();
  }
  
  /**
   * Undoes the given move.
   * @param move The move that needs to be undone.
   */
  public void undoMove(Move move) {
    Move emptyMove = new Move(new Tile(), move.getRow(), move.getColumn());
    if (!getTile(move.getRow(), move.getColumn()).toString().equals(emptyMove.toString())) {
      putTile(emptyMove);
    }
  }
  
  /**
   * Undoes every move of the current turn.
   */
  public void resetTurn() {
    for (Move move : currentLocalTurn) {
      undoMove(move);
    }
    currentLocalTurn = new ArrayList<Move>();
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
    result = "XXX ";
    // This part creates the top row of column numbers
    for (int column = columnMin; column <= columnMax; column++) { 
      if (column > 9) {
        result = result + (column) + "  ";
      } else if (column > 99) {
        result = result + (column) + " ";
      } else {
        result = result + (column) + "  ";
      }
    }
    result = result + "\n";
    for (int row = rowMin; row <= rowMax; row++) {
      if (row > 9) {
        result = result + (row) + "  ";
      } else if (row > 99) {
        result = result + (row) + " ";
      } else {
        result = result + (row) + "   ";
      }
      for (int column = columnMin; column <= columnMax; column++) {
        result = result + boardMatrix.get(row).get(column).toString() + "  ";
      }
      result = result + "\n";
    }
    return result;
  }
  
  /**
   * Checks if the current move is a correct move according to the game rules.
   * @param move The move that needs to be checked.
   * @return True or false whether the move is a correct move or not.
   */
  public Boolean checkMove(Move move) {
    Boolean gotVerticalRow;
    Boolean gotHorizontalRow;
    String empty = ". ";
    Boolean result = true;
    // Check if the destination of the move is 
    if (getTile(move.getRow(), move.getColumn()).toString().equals(empty)) {
      if (currentMovesLineUp(move)) {
        // Check if the current move creates a vertical and horizontal row.
        gotVerticalRow = !getTile(move.getRow() - 1, move.getColumn()).toString().equals(empty) 
            || !getTile(move.getRow() + 1, move.getColumn()).toString().equals(empty);
        gotHorizontalRow = !getTile(move.getRow(), move.getColumn() - 1).toString().equals(empty) 
            || !getTile(move.getRow(), move.getColumn() + 1).toString().equals(empty);
        
        if (gotHorizontalRow) {
          // Check if the current move fits in the horizontal row.
          ArrayList<String> adjesentHorizontalTilesShapes = new ArrayList<String>();
          ArrayList<String> adjesentHorizontalTilesColors = new ArrayList<String>();
          int row = move.getRow();
          int column = move.getColumn() + 1;
          while (!getTile(row, column).toString().equals(empty)) {
            adjesentHorizontalTilesShapes.add(getTile(row, column).getShape());
            adjesentHorizontalTilesColors.add(getTile(row, column).getColor());
            column++;
          }
          column = move.getColumn() - 1;
          while (!getTile(row, column).toString().equals(empty)) {
            adjesentHorizontalTilesShapes.add(getTile(row, column).getShape());
            adjesentHorizontalTilesColors.add(getTile(row, column).getColor());
            column--;
          }
          Boolean shapesAreTheSame = true;
          String templateShape = move.getTile().getShape();
          for (String shape : adjesentHorizontalTilesShapes) {
            shapesAreTheSame = shapesAreTheSame && shape.equals(templateShape);
          }
          shapesAreTheSame = shapesAreTheSame 
              && !adjesentHorizontalTilesColors.contains(move.getTile().getColor());
          
          Boolean colorsAreTheSame = true;
          String templateColor = move.getTile().getColor();
          for (String color : adjesentHorizontalTilesColors) {
            colorsAreTheSame = colorsAreTheSame && color.equals(templateColor);
          }
          colorsAreTheSame = colorsAreTheSame 
              && !adjesentHorizontalTilesShapes.contains(move.getTile().getShape());
          
          if (shapesAreTheSame || colorsAreTheSame) {
            result = true;
          } else {
            result = false;
          }
        }
        
        if (gotVerticalRow) {
          // Check if the current move fits in the vertical row.
          ArrayList<String> adjesentVerticalTilesShapes = new ArrayList<String>();
          ArrayList<String> adjesentVerticalTilesColors = new ArrayList<String>();
          int row = move.getRow() + 1;
          int column = move.getColumn();
          while (!getTile(row, column).toString().equals(empty)) {
            adjesentVerticalTilesShapes.add(getTile(row, column).getShape());
            adjesentVerticalTilesColors.add(getTile(row, column).getColor());
            row++;
          }
          row = move.getRow() - 1;
          while (!getTile(row, column).toString().equals(empty)) {
            adjesentVerticalTilesShapes.add(getTile(row, column).getShape());
            adjesentVerticalTilesColors.add(getTile(row, column).getColor());
            row--;
          }
          Boolean shapesAreTheSame = true;
          String templateShape = move.getTile().getShape();
          for (String shape : adjesentVerticalTilesShapes) {
            shapesAreTheSame = shapesAreTheSame && shape.equals(templateShape);
          }
          shapesAreTheSame = shapesAreTheSame 
              && !adjesentVerticalTilesColors.contains(move.getTile().getColor());
          
          Boolean colorsAreTheSame = true;
          String templateColor = move.getTile().getColor();
          for (String color : adjesentVerticalTilesColors) {
            colorsAreTheSame = colorsAreTheSame && color.equals(templateColor);
          }
          colorsAreTheSame = colorsAreTheSame 
              && !adjesentVerticalTilesShapes.contains(move.getTile().getShape());
          
          if (shapesAreTheSame || colorsAreTheSame) {
            result = result && true;
          } else {
            result = false;
          }
        }
        
        if (!gotHorizontalRow && !gotVerticalRow) {
          result = move.getRow() == 91 && move.getColumn() == 91;
        }
      } else {
        result = false;
      }
    } else {
      result = false;
    }
    return result;
  }
  
  /**
   * Checks if all the done moves this turn and the given
   * move line up in the same row or column.
   * @param move The move that is added this turn.
   * @return True or False whether the moves line up or not.
   */
  public Boolean currentMovesLineUp(Move move) {
    Boolean result = false;
    if (!currentLocalTurn.isEmpty()) {
      Boolean horizontalLineUp = true;
      Move previousMove = move;
      int lowestColumn = move.getColumn();
      int highestColumn = move.getColumn();
      // Check if all done moves and current move are on the same row.
      for (Move doneMove : currentLocalTurn) {
        horizontalLineUp = horizontalLineUp && previousMove.getRow() == doneMove.getRow();
        previousMove = doneMove;
        if (doneMove.getColumn() < lowestColumn) {
          lowestColumn = doneMove.getColumn();
        } else if (doneMove.getColumn() > highestColumn) {
          highestColumn = doneMove.getColumn();
        }
      }
      if (horizontalLineUp) {
        result = highestColumn - lowestColumn == currentLocalTurn.size();
      } else {
        Boolean verticalLineUp = true;
        previousMove = move;
        int lowestRow = move.getRow();
        int highestRow = move.getRow();
        // Check if all done moves and current move are on the same column.
        for (Move doneMove : currentLocalTurn) {
          verticalLineUp = verticalLineUp && previousMove.getColumn() == doneMove.getColumn();
          previousMove = doneMove;
          if (doneMove.getRow() < lowestRow) {
            lowestRow = doneMove.getRow();
          } else if (doneMove.getRow() > highestRow) {
            highestRow = doneMove.getRow();
          }
        }
        if (verticalLineUp) {
          result = highestRow - lowestRow == currentLocalTurn.size();
        }
      }
    } else {
      result = true;
    }
    return result;
  }
  
  /**
   * Calculates the amount of points that are earned with
   * the current turn.
   * @return The calculated the amount of points that are 
   *         earned with the current turn.
   */
  public int getScoreCurrentTurn() {
    String empty = ". ";
    int verticalResult = 0;
    int horizontalResult = 0;
    for (Move move : currentLocalTurn) {
      Boolean gotVerticalRow;
      Boolean gotHorizontalRow;
      // Check in which directions the move makes rows.
      gotVerticalRow = !getTile(move.getRow() - 1, move.getColumn()).toString().equals(empty) 
          || !getTile(move.getRow() + 1, move.getColumn()).toString().equals(empty);
      gotHorizontalRow = !getTile(move.getRow(), move.getColumn() - 1).toString().equals(empty) 
          || !getTile(move.getRow(), move.getColumn() + 1).toString().equals(empty);
      int verticalPoints = 0;
      int horizontalPoints = 0;
      if (gotVerticalRow) {
        int row = move.getRow() + 1;
        int column = move.getColumn();
        verticalPoints++;
        while (!getTile(row, column).toString().equals(empty)) {
          verticalPoints++;
          row++;
        }
        row = move.getRow() - 1;
        while (!getTile(row, column).toString().equals(empty)) {
          verticalPoints++;
          row--;
        }
        if (verticalPoints == 6) {
          verticalPoints += 6;
        }
      }
      if (gotHorizontalRow) {
        int row = move.getRow();
        int column = move.getColumn() + 1;
        horizontalPoints++;
        while (!getTile(row, column).toString().equals(empty)) {
          horizontalPoints++;
          column++;
        }
        column = move.getColumn() - 1;
        while (!getTile(row, column).toString().equals(empty)) {
          horizontalPoints++;
          column--;
        }
        if (horizontalPoints == 6) {
          horizontalPoints += 6;
        }
      }
      horizontalResult += horizontalPoints;
      verticalResult += verticalPoints;
      if (!gotHorizontalRow && !gotVerticalRow) {
        horizontalResult = 1;
      }
    }
    Boolean horizontalLineUp = true;
    Move previousMove = currentLocalTurn.get(0);
    for (Move doneMove : currentLocalTurn) {
      horizontalLineUp = horizontalLineUp && previousMove.getRow() == doneMove.getRow();
      previousMove = doneMove;
    }
    if (currentLocalTurn.size() > 1) {
      // Get rid of double counted points.
      if (horizontalLineUp) {
        horizontalResult = horizontalResult / currentLocalTurn.size();
      } else {
        verticalResult = verticalResult / currentLocalTurn.size();
      }
    }
    
    return horizontalResult + verticalResult;
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
    board.putTile(move);
    board.putTile(move2);
    board.putTile(move3);
    System.out.print(board.toString());
  }
}
