package client;

import java.util.List;
import java.util.Map;

import exceptions.HandIsFullException;
import exceptions.InvalidMoveException;
import exceptions.TileNotInHandException;

public class ComputerPlayer extends Player {
  
  private Strategy strategy;
  private List<Tile> hand;
 
  public ComputerPlayer(String name, int playerNumber, Strategy strategy) {
    super(name, playerNumber);
    this.strategy = strategy;
  }

  public Move determineMove(Board board) throws InvalidMoveException {
    return strategy.determineMove(board);
  }

  

  
}
