package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import client.Move.Type;
import exceptions.HandIsFullException;
import exceptions.InvalidMoveException;
import exceptions.TileNotInHandException;

public class ComputerPlayer extends Player {
  
  private Strategy strategy;
  private boolean madeMove = false;
 
  public ComputerPlayer(String name, int playerNumber, Strategy strategy) {
    super(name, playerNumber);
    this.strategy = strategy;
  }

  public Move determineMove(Board board) throws InvalidMoveException {
    Move result = null;
    System.out.println("detmov");
    if (!madeMove) {      
      System.out.println("mov mov");
      result = strategy.determineMove(board, getHand(), this);
      try {
        removeFromHand(result.getTile());
      } catch (TileNotInHandException e) {
        Printer.print(e);
      }
      getMoves().add(result);
    } else if (madeMove) {
      System.out.println("end mov");
      result = new Move();
    } 
    return result;
  }
  
  public void setMadeMove(boolean bool) {
    madeMove = bool;
  }


  

  
}
