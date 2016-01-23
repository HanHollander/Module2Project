package server.model;

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
      throw new TileNotInHandException(new client.model.Tile(tile.getColor(), tile.getShape()));
    }
  }
  
  
    
  //Getters, toString\\
        
  /**
   * Get the name of the player.
   * @return name
   */
  /*@ pure */ public String getName() {
    return name;
  }
  
  /**
   * Get the hand of the player.
   * @return hand
   */
  /*@ pure */ public List<Tile> getHand() {
    List<Tile> result = new ArrayList<Tile>();
    result.addAll(hand);
    return result;
  }
  
  /**
   * Return a string representation of the hand.
   * @return string representation of the hand
   *         " (tile) (tile) (tile) (tile) (tile) (tile)"
   */
  public String handToString() {
    String result = "";
    for (Tile tile : hand) {
      result = result + " " + tile.toString();
    }
    return result;
  }
    
  /**
   * get number of the player.
   * @return playerNumber
   */
  /*@ pure */ public int getPlayerNumber() {
    return playerNumber;
  }
  
  public String toString() {
    return "Player-" + playerNumber + " " + name;
  }
  
  /*@ pure */ public int getScore() {
    return score;
  }
  
  public void addToScore(int extraPoints) {
    score += extraPoints;
  }
    
}
