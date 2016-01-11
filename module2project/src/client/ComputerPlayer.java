package client;

import java.util.Map;

public class ComputerPlayer extends Player {
  
  private Strategy strategy;
  private Tile[] hand;
 
  public ComputerPlayer(String name, int playerNumber, Strategy strategy) {
    super(name, playerNumber);
    this.strategy = strategy;
  }

  public Map<int[], String> determineMove() {
    return strategy.determineMove();
  }
}
