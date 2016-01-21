package client.model;

import exceptions.InvalidMoveException;

public class OpponentPlayer extends Player {
  
  

  public OpponentPlayer(String name, int playerNumber) {
    super(name, playerNumber);
  }

  //NEVER USED!
  public Move determineMove(Board board) throws InvalidMoveException {
    return null;
  }
  
}
