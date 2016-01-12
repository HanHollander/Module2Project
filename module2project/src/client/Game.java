package client;

import java.util.List;

import exceptions.InvalidMoveException;

public class Game {
    
  //Fields\\
  
  private Board board;
    
  private List<Player> playerList;
  private Player player;
  
  //Constructor\\
  
  public Game(List<Player> playerList, Player player) {
    this.playerList = playerList;
    this.player = player;
  }
  
  public void makeMove(Board board) {
    Move move = null;
    try {
      move = player.determineMove(board);
    } catch (InvalidMoveException e) {
      System.out.println("This is NOT a valid move.");
    }
    board.put(move);
  }
    
    

}
