package client.model;

import exceptions.HandIsFullException;
import exceptions.InvalidMoveException;
import exceptions.TileNotInHandException;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    
  private String name;
  private int playerNumber;
  private List<Tile> hand;
  private List<Move> moves;
  private int score;
  private boolean madeMove;
        
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
        
  public Player() {
    this.name = "hintGen";
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
  
  /**
   * Returns a nice list with colour.
   * @return nice list
   */
  public String handToString() {
    String result = "[";
    int index = 1;
    if (hand.size() > 0) {
      for (Tile tile : hand) {
        result = result + index + ": " + tile.colourToString() + ", ";
        index++;
      }
      result = result.substring(0, result.length() - 2);
    }
    result = result + "]";
    return result;
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

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public boolean isMadeMove() {
    return madeMove;
  }

  public void setMadeMove(boolean madeMove) {
    this.madeMove = madeMove;
  }
    
}
