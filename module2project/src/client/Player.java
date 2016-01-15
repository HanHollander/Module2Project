package client;

import java.util.ArrayList;
import java.util.List;

import exceptions.HandIsFullException;
import exceptions.InvalidMoveException;
import exceptions.TileNotInHandException;

public abstract class Player {
    
  private String name;
  private int playerNumber;
  private List<Tile> hand;
  private List<Move> moves;
        
  /**
   * Constructor for a Player.
   * @param name the name
   * @param playerNumber the number
   */
  public Player(String name, int playerNumber) {
    this.name = name;
    this.playerNumber = playerNumber;
    this.hand = new ArrayList<Tile>();
    this.setMoves(new ArrayList<Move>());
  }
        
  /**
   * Function that determines what move is played next.
   * @throws InvalidMoveException invalid move
   */
  public abstract Move determineMove(Board board) throws InvalidMoveException;
  
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
    boolean contains = false;
    Tile remove = null;
    for (Tile check : hand) {
      if (check.toString().equals(tile.toString())) {
        remove = check;
        contains = true;
      }
    }
    if (contains) {
      hand.remove(remove);
    } else {
      throw new TileNotInHandException(tile);
    }
  }
  
  public String getName() {
    return name;
  }
  
  public List<Tile> getHand() {
    return hand;
  }
  
  public String handToString() {
    return hand.toString();
  }
    
  public int getPlayerNumber() {
    return playerNumber;
  }
  
  public String toString() {
    return "Player " + playerNumber + ": " + name;
  }

  public List<Move> getMoves() {
    return moves;
  }

  public void setMoves(List<Move> moves) {
    this.moves = moves;
  }
    
}
