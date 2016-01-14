package client.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import org.junit.Before;
import org.junit.Test;

import client.Board;
import client.HumanPlayer;
import client.Move;
import client.Tile;
import exceptions.HandIsFullException;
import exceptions.InvalidCommandException;
import exceptions.InvalidMoveException;

public class HumanPlayerTest {
  
  private String validMoveCommand = "MOVE Ro 40 40";
  private String invalidMoveCommand1 = "MOVE AB 40 40";
  private String invalidMoveCommand2 = "MOVE Ro 40 ds";
  private String invalidMoveCommand3 = "MOVE Ro ds 40";
  private String invalidMoveCommand4 = "Move Ro 40 40 40";
  
  private String validSwapCommand = "SWAP Ro";
  private String invalidSwapCommand1 = "SWAP r0";
  private String invalidSwapCommand2 = "SWAP Ro Ro";
  
  private String validEndCommand = "END";
  private String invalidEndCommand = "ENDd";
  
  private HumanPlayer p;
  private Board b;
  
  
  @Before
  public void setUp() throws Exception {
    p = new HumanPlayer("P", 1);
    b = new Board();
  }
  
  
  
  @Test
  public void testDetermineMove1() throws HandIsFullException, InvalidMoveException {
    p.addToHand(new Tile("R", "o"));
    p.addToHand(new Tile("B", "o"));
    p.addToHand(new Tile("Y", "o"));
    p.addToHand(new Tile("R", "s"));
    
    ByteArrayInputStream in = new ByteArrayInputStream("MOVE Ro 91 91".getBytes());  
    System.setIn(in);  
    p.determineMove(b); 
    
    ByteArrayInputStream in2 = new ByteArrayInputStream("MOVE Bo 91 91".getBytes());  
    System.setIn(in2);  
    p.determineMove(b); 
    
    ByteArrayInputStream in3 = new ByteArrayInputStream("END".getBytes());  
    System.setIn(in3);  
    p.determineMove(b); 
    
    
    System.setIn(System.in);
  }
  
  @Test
  public void testDetermineMove2() throws HandIsFullException, InvalidMoveException {
    p.addToHand(new Tile("R", "o"));
    p.addToHand(new Tile("B", "o"));
    p.addToHand(new Tile("Y", "o"));
    
    ByteArrayInputStream in = new ByteArrayInputStream("SWAP Ro".getBytes());  
    System.setIn(in);  
    p.determineMove(b); 
    
    ByteArrayInputStream in2 = new ByteArrayInputStream("SWAP Bo".getBytes());  
    System.setIn(in2);  
    p.determineMove(b); 
    
    System.setIn(System.in);
  }
  
  
  
  
  
  
  
  
  
  @Test
  public void testVerifyMove() throws InvalidCommandException {
    boolean valid = true;
    assertTrue(p.verifyMove(validMoveCommand).equals(new Move(new Tile("R", "o"), 40, 40)));
    assertTrue(p.verifyMove(validSwapCommand).equals(new Move(new Tile("R", "o"))));
    assertTrue(p.verifyMove(validEndCommand).equals(new Move()));
    try {
      p.verifyMove("End");
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertFalse(valid);
  }

  @Test
  public void testValidMoveCommand() {
    boolean valid = true;
    try {
      p.validMoveCommand(validMoveCommand);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertTrue(valid);
    try {
      p.validMoveCommand(invalidMoveCommand1);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertFalse(valid);
    valid = true;
    try {
      p.validMoveCommand(invalidMoveCommand2);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertFalse(valid);
    valid = true;
    try {
      p.validMoveCommand(invalidMoveCommand3);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertFalse(valid);
    valid = true;
    try {
      p.validMoveCommand(invalidMoveCommand4);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertFalse(valid);
  }
  
  @Test
  public void testValidSwapCommand() {
    boolean valid = true;
    try {
      p.validSwapCommand(validSwapCommand);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertTrue(valid);
    try {
      p.validSwapCommand(invalidSwapCommand1);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertFalse(valid);
    valid = true;
    try {
      p.validSwapCommand(invalidSwapCommand2);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertFalse(valid);
  }
  
  @Test
  public void testValidEndCommand() {
    boolean valid = true;
    try {
      p.validEndCommand(validEndCommand);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertTrue(valid);
    try {
      p.validEndCommand(invalidEndCommand);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertFalse(valid);
  }
}
