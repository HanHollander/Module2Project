package client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import client.model.Move.Type;
import client.view.Printer;
import exceptions.HandIsFullException;
import exceptions.InvalidMoveException;
import exceptions.TileNotInHandException;

public class ComputerPlayer extends Player {
  
  private Strategy strategy;
 
  public ComputerPlayer(String name, int playerNumber, Strategy strategy) {
    super(name, playerNumber);
    this.strategy = strategy;
  }

  /**
   * Determines the next move the player is going to make.
   * @param board the board.
   * @return the move.
   */
  //@ensures board.checkMove(\result);
  public Move determineMove(Board board) throws InvalidMoveException {
    Move result = null;
    if (!isMadeMove()) {      
      result = strategy.determineMove(board, getHand(), this);
      try {
        removeFromHand(result.getTile());
      } catch (TileNotInHandException e) {
        Printer.print(e);
      }
      getMoves().add(result);
    } else if (isMadeMove()) {
      result = new Move();
      setMadeMove(false);
    } 
    return result;
  }


  

  
}
