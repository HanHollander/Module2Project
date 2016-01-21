package client.test.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import client.model.ComputerPlayer;
import client.model.HumanPlayer;
import client.model.Player;
import client.model.Tile;
import exceptions.HandIsFullException;
import exceptions.TileNotInHandException;

public class PlayerTest {

  //handtostring removehand addhand
  
  private Player p;
  
  @Before
  public void setUp() throws Exception {
    p = new HumanPlayer("p", 1, null);
  }

  @Test
  public void testAddToHand1() throws HandIsFullException {
    Tile tile1 = new Tile("R", "o");
    p.addToHand(tile1);
    List<Tile> hand1 = new ArrayList<Tile>();
    hand1.add(tile1);
    assertTrue(p.getHand().equals(hand1));
  }
  
  @Test
  public void testAddToHand2() {
    boolean handFull = false;
    Tile tile1 = new Tile("R", "o");
    Tile tile2 = new Tile("B", "o");
    Tile tile3 = new Tile("G", "o");
    Tile tile4 = new Tile("Y", "o");
    Tile tile5 = new Tile("P", "o");
    Tile tile6 = new Tile("O", "o");
    Tile tile7 = new Tile("R", "x");
    try {
      p.addToHand(tile1);
      p.addToHand(tile2);
      p.addToHand(tile3);
      p.addToHand(tile4);
      p.addToHand(tile5);
      p.addToHand(tile6);
      p.addToHand(tile7);
    } catch (HandIsFullException e) {
      handFull = true;
    }
    assertTrue(handFull);
  }
  
  @Test
  public void testRemoveFromHand1() throws HandIsFullException, TileNotInHandException {
    Tile tile1 = new Tile("R", "o");
    p.addToHand(tile1);
    p.removeFromHand(tile1);
    List<Tile> hand1 = new ArrayList<Tile>();
    assertTrue(p.getHand().equals(hand1));
  }
  
  @Test
  public void testRemoveFromHand2() {
    Tile tile1 = new Tile("R", "o");
    boolean inHand = true;
    try {
      p.removeFromHand(tile1);
    } catch (TileNotInHandException e) {
      inHand = false;
    }
    assertFalse(inHand);
  }
  
  @Test
  public void testHandToString() throws HandIsFullException {
    String string = "[1: " + Tile.ANSI_RED + "A" + Tile.ANSI_RESET + "]";
    Tile tile1 = new Tile("R", "o");
    p.addToHand(tile1);
    assertTrue(p.handToString().equals(string));
  }

}
