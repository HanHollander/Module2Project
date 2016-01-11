package client;

import java.util.Map;

import exceptions.InvalidMoveException;

/**
 * Abstract player class.
 * @author Han Hollander
 */

public abstract class Player {
    
  //Fields\\
    
  private String name;
  private int playerNumber;
    
  //Constructor\\
    
  /**
   * Constructor for a Player.
   * @param name the name
   * @param playerNumber the number
   */
  public Player(String name, int playerNumber) {
    this.name = name;
    this.playerNumber = playerNumber;
  }
    
  //Functions\\
    
  /**
   * Function that determines what move is played next.
   * @throws InvalidMoveException invalid move
   */
  public abstract Move determineMove(Board board) throws InvalidMoveException;
    
  //Getters\\
        
  /**
   * Get the name of the player.
   * @return name
   */
  public String getName() {
    return name;
  }
    
  /**
   * get number of the player.
   * @return playerNumber
   */
  public int getPlayerNumber() {
    return playerNumber;
  }
  
  public String toString() {
    return "Player " + playerNumber + ": " + name;
  }
    
}
