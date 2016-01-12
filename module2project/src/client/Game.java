package client;

import java.util.ArrayList;
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
  
  public void playerTurn() {
    setPlayerTurn(true);
    while (playerTurn) {
      System.out.println(player.handToString());
      makeMove(board);
      System.out.println(board.toString());
    }
    endTurn();
  }
  
  public void endTurn() {
    String listType = player.getMoves().get(0).getType().toString();
    String command = listType + " ";
    if (listType == "MOVE") {
      for (Move move : player.getMoves()) {
        command = command + move.getTile().toString() 
            + " " + move.getRow() + " " + move.getColumn() + " ";
      }
      System.out.println("Score :" + board.getScoreCurrentTurn());
    } else if (listType == "SWAP") {
      for (Move move : player.getMoves()) {
        command = command + move.getTile().toString() + " ";
      }
    }
    System.out.println("Command: " + command);
    player.setMoves(new ArrayList<Move>());
    board.endTurn();
    opponentTurn();//opponentTurn
  }
  
  public void opponentTurn() {
    
    
    
    playerTurn();
  }

  public void makeMove(Board board) {
    Move move = null;
    try {
      move = player.determineMove(board);
      if (move.getType().equals(Type.MOVE)) {
        board.putTile(move);
      } else if (move.getType().equals(Type.SWAP)) {
        //Send command
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
