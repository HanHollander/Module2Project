package server.test.controller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import exceptions.HandIsFullException;
import server.controller.*;
import server.model.Move;
import server.model.Tile;

public class ClientHandlerTest {

  private Server s;
  private ClientHandler ch;
  
  @Before
  public void setUp() {
    s = new Server(null, 2, 999999, null, 1);
    s.getGame().addPlayer(1, "Test");
    try {
      s.getGame().getPlayer(1).addToHand(new Tile("P", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("G", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("G", "x"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    ch = new ClientHandler(1, s);
    assertEquals(null, ch.getClientName());
  }
  
  @Test
  public void testConvertStringToMoveTurn() {
    List<Move> turn1 = new ArrayList<Move>();
    turn1.add(new Move(new Tile("P", "o"), 91, 91));
    assertEquals(turn1.toString(), ch.convertStringToMoveTurn("MOVE Po 91 91").toString());
    List<Move> turn2 = new ArrayList<Move>();
    turn2.add(new Move(new Tile("P", "o"), 91, 91));
    turn2.add(new Move(new Tile("P", "x"), 91, 92));
    assertEquals(turn2.toString(), ch.convertStringToMoveTurn("MOVE Po 91 91 Px 91 92").toString());
  }
  
  @Test
  public void testConvertStringToSwapTurn() {
    List<Tile> turn1 = new ArrayList<Tile>();
    turn1.add(new Tile("P", "o"));
    assertEquals(turn1.toString(), ch.convertStringToSwapTurn("SWAP Po").toString());
    List<Tile> turn2 = new ArrayList<Tile>();
    turn2.add(new Tile("P", "o"));
    turn2.add(new Tile("P", "x"));
    assertEquals(turn2.toString(), ch.convertStringToSwapTurn("SWAP Po Px").toString());
  }
  
  @Test
  public void testIsValidStartMessage() {
    assertTrue(ch.isValidStartMessage("HELLO aAbB"));
    assertTrue(ch.isValidStartMessage("HELLO a"));
    assertTrue(ch.isValidStartMessage("HELLO abcdefghABCDEFGH"));
    assertFalse(ch.isValidStartMessage("HELLO aAbB1"));
    assertFalse(ch.isValidStartMessage("HELLO a1"));
    assertFalse(ch.isValidStartMessage("HELLO "));
    assertFalse(ch.isValidStartMessage("HELLO"));
    assertFalse(ch.isValidStartMessage("hello aB"));
    assertFalse(ch.isValidStartMessage("abcd"));
  }
  
  @Test
  public void testIsValidMoveTurnMessage() {
    assertTrue(ch.isValidMoveTurnMessage("MOVE Po 91 91"));
    assertTrue(ch.isValidMoveTurnMessage("MOVE Gx 91 91"));
    assertTrue(ch.isValidMoveTurnMessage("MOVE Po 91 91 Go 91 92"));
    assertFalse(ch.isValidMoveTurnMessage("MOVE Po 91 91 Gx 91 92"));
    assertFalse(ch.isValidMoveTurnMessage("MOVE Yo 91 91"));
    assertFalse(ch.isValidMoveTurnMessage("Po 91 91"));
    assertFalse(ch.isValidMoveTurnMessage("MOVE Po -1 91"));
    assertFalse(ch.isValidMoveTurnMessage("MOVE Po 200 91"));
    assertFalse(ch.isValidMoveTurnMessage("MOVE Po 91 -1"));
    assertFalse(ch.isValidMoveTurnMessage("MOVE Po 91 200"));
    assertFalse(ch.isValidMoveTurnMessage("MOVE aB"));
    assertFalse(ch.isValidMoveTurnMessage("MOVE"));
    assertFalse(ch.isValidMoveTurnMessage("move Po 91 91"));
    assertFalse(ch.isValidMoveTurnMessage("MOVE Po 91"));
    assertFalse(ch.isValidMoveTurnMessage("MOVE Po"));
    assertFalse(ch.isValidMoveTurnMessage("MOVE 91 91"));
    assertFalse(ch.isValidMoveTurnMessage("MOVE91 91"));
    assertFalse(ch.isValidMoveTurnMessage("MOVE a 91 91"));
    assertFalse(ch.isValidMoveTurnMessage("MOVE Po a b"));
    assertFalse(ch.isValidMoveTurnMessage("MOVE aa 91 91 Gx 91 92"));
    assertFalse(ch.isValidMoveTurnMessage("MOVE ao 91 91 Gx 91 92"));
    assertFalse(ch.isValidMoveTurnMessage("MOVE ao 91 91 jj 91 92"));
    assertFalse(ch.isValidMoveTurnMessage("SWAP Po"));
  }
  
  @Test
  public void testIsValidSwapTurnMessage() {
    assertFalse(ch.isValidSwapTurnMessage("MOVE Po 91 91"));
    assertTrue(ch.isValidSwapTurnMessage("SWAP Gx"));
    assertTrue(ch.isValidSwapTurnMessage("SWAP Po Go"));
    assertFalse(ch.isValidSwapTurnMessage("SWAP Po Yo"));
    assertFalse(ch.isValidSwapTurnMessage("SWAP Yo"));
    assertFalse(ch.isValidSwapTurnMessage("HELLO Po"));
    assertFalse(ch.isValidSwapTurnMessage("SWAP aB"));
    assertFalse(ch.isValidSwapTurnMessage("SWAP"));
    assertFalse(ch.isValidSwapTurnMessage("SWAP Po Yo Go Gx Yx Pc Oo"));
    assertFalse(ch.isValidSwapTurnMessage("swap Po"));
    assertFalse(ch.isValidSwapTurnMessage("Po"));
    assertFalse(ch.isValidSwapTurnMessage("SWAP Po 91"));
    assertFalse(ch.isValidSwapTurnMessage("SWAP PX"));
    assertFalse(ch.isValidSwapTurnMessage("SWAP 91 91"));
    assertFalse(ch.isValidSwapTurnMessage("SWAPPo"));
    assertFalse(ch.isValidSwapTurnMessage("SWAP a 91 91"));
    assertFalse(ch.isValidSwapTurnMessage("SWAP Po a b"));
    assertFalse(ch.isValidSwapTurnMessage("SWAP aa Gx"));
    assertFalse(ch.isValidSwapTurnMessage("SWAP ao Gx"));
    assertFalse(ch.isValidSwapTurnMessage("SWAP ao jj"));
  }
}
