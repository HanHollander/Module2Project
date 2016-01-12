package client;

import java.util.List;

import client.Move.Type;
import exceptions.HandIsFullException;
import exceptions.InvalidMoveException;
import exceptions.TileNotInHandException;

public class Game {
    
  //Fields\\
  
  private Board board;
    
  private List<Player> playerList;
  private Player player;
  
  private boolean playerTurn = false;
  
  //Constructor\\
  
  public Game(List<Player> playerList, Player player) {
    this.setPlayerList(playerList);
    this.player = player;
    this.board = new Board();
  }
  
  public void turn() {
    setPlayerTurn(true);
    while (playerTurn) {
      System.out.println(player.handToString());
      makeMove(board);
      System.out.println(board.toString());
    }
    endTurn();
  }
  
  private void endTurn() {
    try {
      player.addToHand(new Tile("R", "*"));
      player.addToHand(new Tile("R", "s"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    board.endTurn();
    turn();
  }

  public void makeMove(Board board) {
    Move move = null;
    try {
      move = player.determineMove(board);
      if (move.getType().equals(Type.MOVE)) {
        System.out.println("Move = " + move.toString());
        board.putTile(move);
      } else if (move.getType().equals(Type.SWAP)) {
        //SEND command to server
      } else if (move.getType().equals(Type.END)) {
        setPlayerTurn(false);
      }
    } catch (InvalidMoveException e) {
      System.out.println(e);
    }
  }

  public Board getBoard() {
    return board;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

  public List<Player> getPlayerList() {
    return playerList;
  }

  public void setPlayerList(List<Player> playerList) {
    this.playerList = playerList;
  }
  
  public void setPlayerTurn(boolean bool) {
    playerTurn = bool;
  }
 
    
    

}
