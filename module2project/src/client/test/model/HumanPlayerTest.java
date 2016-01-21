package client.test.model;

import static org.junit.Assert.*;

import java.io.BufferedWriter;

import org.junit.Before;
import org.junit.Test;

import client.controller.Game;
import client.model.Board;
import client.model.HumanPlayer;
import client.model.Move;
import client.model.Move.Type;
import client.model.Player;
import client.model.Tile;
import exceptions.InvalidCommandException;
import exceptions.InvalidMoveException;

public class HumanPlayerTest {
  
  private HumanPlayer p;
  private Board b;
  private boolean valid;
  private Game g;
  
  @Before
  public void setUp() throws Exception {
    p = new HumanPlayer("p", 1, g);
    p.addToHand(new Tile("S", "o"));
    p.addToHand(new Tile("B", "o"));
    p.addToHand(new Tile("Y", "o"));
    p.addToHand(new Tile("G", "o"));
    b = new Board();
    valid = true;
  }

  @Test
  public void testValidMoveCommand() {
    String command1 = "MOVE 2 91 91";
    String command2 = "MOVE 2 91 ee";
    String command3 = "MOVE 2 91";
    String command4 = "MOVE 91 91 1";
    String command5 = "MOVE 1 91 91";
    try {
      p.validMoveCommand(command1);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertTrue(valid);
    try {
      p.validMoveCommand(command2);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertFalse(valid);
    try {
      p.validMoveCommand(command3);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertFalse(valid);
    try {
      p.validMoveCommand(command4);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertFalse(valid);
    try {
      p.validMoveCommand(command5);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertFalse(valid);
  }
  
  @Test
  public void testValidSwapCommand() {
    String command1 = "SWAP 2";
    String command2 = "SWAP a";
    String command3 = "SWAP 2 2";
    String command4 = "SWAP 1";
    try {
      p.validSwapCommand(command1);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertTrue(valid);
    try {
      p.validSwapCommand(command2);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertFalse(valid);
    try {
      p.validSwapCommand(command3);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertFalse(valid);
    try {
      p.validSwapCommand(command4);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertFalse(valid);
    assertFalse(valid);
  }
  
  @Test
  public void testValidEndCommand() {
    String command1 = "END";
    String command2 = "ENDe";
    try {
      p.validEndCommand(command1);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertTrue(valid);
    try {
      p.validEndCommand(command2);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertFalse(valid);
  }
  
  @Test
  public void testVerifyMove() throws InvalidCommandException {
    String command1 = "MOVE 2 91 91";
    String command2 = "SWAP 2";
    String command3 = "END";
    Move move1 = new Move(new Tile("B", "o"), 91, 91);
    Move move2 = new Move(new Tile("B", "o"));
    Move move3 = new Move();
    assertTrue(p.verifyMove(command1).equals(move1));
    assertTrue(p.verifyMove(command2).equals(move2));
    assertTrue(p.verifyMove(command3).equals(move3));
  }
  
  @Test
  public void testGetMove() throws InvalidCommandException {
    p.getMove();
    //I type in console: MOVE 2 91 91
    //It should work, i.e. no IOException!
  }
  
  @Test
  public void testDetermineMove1() throws InvalidMoveException {
    Move move1 = p.determineMove(b);
    assertTrue(p.getListType().equals(Type.ANY));
    //I type in console: MOVE 2 91 91
    assertTrue(move1.getType().equals(Type.MOVE));
    Move move2 = p.determineMove(b);
    assertTrue(p.getListType().equals(Type.MOVE));
    //I type in console: END
    assertTrue(move2.getType().equals(Type.END));
  }
  
  @Test
  public void testDetermineMove2() throws InvalidMoveException {
    Move move1 = p.determineMove(b);
    assertTrue(p.getListType().equals(Type.ANY));
    //I type in console: SWAP 2
    assertTrue(move1.getType().equals(Type.SWAP));
    Move move2 = p.determineMove(b);
    assertTrue(p.getListType().equals(Type.SWAP));
    //I type in console: END
    assertTrue(move2.getType().equals(Type.END));
  }
  
  
  
  
  
  
  
  
  

}
