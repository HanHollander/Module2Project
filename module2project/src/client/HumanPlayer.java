package client;

import java.util.Map;

public class HumanPlayer extends Player {
  //tile?
  //row?
  //column? inputs System.in

  public HumanPlayer(String name, int playerNumber) {
    super(name, playerNumber);
  }

  public Map<int[], String> determineMove(Board board) {
    //get input
    boolean validMove = false;
    Map<int[], String> result = null;
    while (!validMove) {
      //get input
      if (board.checkMove(Tile tile, int row, int column)) {
        validMove = true;
      } else {
        //print message
      }
    }
    return result;;
  }

}
