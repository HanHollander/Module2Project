package client;

import java.util.Map;

import exceptions.InvalidMoveException;

public class ComputerPlayer extends Player {
  
  private Strategy strategy;
  private Tile[] hand;
 
  public ComputerPlayer(String name, int playerNumber, Strategy strategy) {
    super(name, playerNumber);
    this.strategy = strategy;
  }

  @Override
  public Move determineMove(Board board) throws InvalidMoveException {
    return strategy.determineMove(board);
  }

  

  
}
