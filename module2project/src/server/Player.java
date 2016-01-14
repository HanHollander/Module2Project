package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import exceptions.HandIsFullException;
import exceptions.InvalidMoveException;
import exceptions.TileNotInHandException;

/**
 * Abstract player class.
 * @author Han Hollander
 */

public class Player {
    
  //Fields\\
    
  private String name;
  private int playerNumber;
  private List<Tile> hand;
  private List<Move> moves;
  private int score;
    
  //Constructor\\
    
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
    score = 0;
  }
    
  //Functions\\
  
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
      throw new TileNotInHandException(new client.Tile(tile.getColor(), tile.getShape()));
    }
  }
  
  
    
  //Getters, toString\\
        
  /**
   * Get the name of the player.
   * @return name
   */
  public String getName() {
    return name;
  }
  
  public List<Tile> getHand() {
    return hand;
  }
  
  public String handToString() {
    return "Hand: " + getHand();
  }
    
  /**
   * get number of the player.
   * @return playerNumber
   */
  public int getPlayerNumber() {
    return playerNumber;
  }
  
  public String toString() {
    return "Player-" + playerNumber + " " + name;
  }

  public List<Move> getMoves() {
    return moves;
  }

  public void setMoves(List<Move> moves) {
    this.moves = moves;
  }
  
  public int getScore() {
    return score;
  }
  
  public void addToScore(int extraPoints) {
    score += extraPoints;
  }
    
}
