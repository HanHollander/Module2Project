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

  /**
   * Add a tile to a hand.
   * @param tile the tile to add
   * @throws HandIsFullException if hand is full
   */
  public void addToHand(Tile tile) throws HandIsFullException {
    if (hand.size() < 6) {
      hand.add(tile);
    } else {
      throw new HandIsFullException();
    }
  }
  
  /**
   * Remove tile from hand.
   * @param tile tile to remove
   * @throws TileNotInHandException tile is not in hand
   */
  public void removeFromHand(Tile tile) throws TileNotInHandException {
    if (hand.contains(tile)) {
      hand.remove(tile);
    } else {
      throw new TileNotInHandException(tile);
    }
  }

  
}
