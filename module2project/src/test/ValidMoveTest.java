package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import client.HumanPlayer;
import exceptions.InvalidCommandException;

public class ValidMoveTest {
  
  private String validMoveCommand = "MOVE Ro 40 40";
  private String invalidMoveCommand1 = "MOVE AB 40 40";
  private String invalidMoveCommand2 = "MOVE Ro 40 ds";
  private String invalidMoveCommand3 = "MOVE Ro ds 40";
  private String validSwapCommand = "SWAP Ro";
  private String invalidSwapCommand = "SWAP r0";
  
  
  @Before
  public void setUp() throws Exception {
    
  }

  @Test
  public void testValidMoveCommand() {
    boolean valid = true;
    HumanPlayer p = new HumanPlayer("P", 1);
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
  }
  
  @Test
  public void testValidSwapCommand() {
    boolean valid = true;
    HumanPlayer p = new HumanPlayer("P", 1);
    try {
      p.validSwapCommand(validSwapCommand);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertTrue(valid);
    try {
      p.validSwapCommand(invalidSwapCommand);
    } catch (InvalidCommandException e) {
      valid = false;
    }
    assertFalse(valid);
  }
}
