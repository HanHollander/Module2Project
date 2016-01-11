package client;

import java.util.ArrayList;
import java.util.List;

public class Board {
  
  private List<List<Tile>> boardMatrix;
  
  /**
   * Board constructor. Constructs a empty board.
   */
  public Board() {
    for (int row = 0; row < 185; row++) {
      boardMatrix.add(new ArrayList<Tile>());
      for (int collumn = 0; collumn < 185; collumn++) {
        boardMatrix.get(row).add(new Tile());
      }
    }
  }
  
  public List<List<Integer>> getMarges() {
    
    return null;
  }
  
  public Boolean checkMove(Tile tile, int row, int collumn) {
    return true;
  }
  
}
