package client.test.model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import client.model.Board;
import client.model.ComputerPlayer;
import client.model.Move;
import client.model.NaiveStrategy;
import client.model.Tile;
import exceptions.TileNotInHandException;

public class NaiveStrategyTest {

  private NaiveStrategy ns;
  private Board b;
  private ComputerPlayer p;
  private List<Tile> h;
  
  @Before
  public void setUp() throws Exception {
    ns = new NaiveStrategy();
    b = new Board();
    p = new ComputerPlayer("p", 1, ns);
    p.addToHand(new Tile("R", "a"));
    p.addToHand(new Tile("B", "a"));
    p.addToHand(new Tile("Y", "o"));
    p.addToHand(new Tile("G", "o"));
    h = p.getHand();
  }

  @Test
  public void testDetermineMove() throws TileNotInHandException {
    Tile tile1 = new Tile("G", "o");
    Tile tile2 = new Tile("Y", "o");
    Tile tile3 = new Tile("R", "a");
    Move move1 = new Move(tile1, 91, 91);
    Move move2 = new Move(tile2, 91, 92);
    Move move3 = new Move(tile3);
    assertTrue(ns.determineMove(b, h, p).equals(move1));
    p.removeFromHand(tile1);
    b.putTile(move1);
    assertTrue(ns.determineMove(b, h, p).equals(move2));
    p.removeFromHand(tile2);
    b.putTile(move2);
    assertTrue(ns.determineMove(b, h, p).equals(move3));
  }
  
  @Test
  public void testGetHint() throws TileNotInHandException {
    Tile tile1 = new Tile("G", "o");
    Tile tile2 = new Tile("Y", "o");
    Tile tile3 = new Tile("R", "a");
    Move move1 = new Move(tile1, 91, 91);
    Move move2 = new Move(tile2, 91, 92);
    Move move3 = new Move(tile3);
    assertTrue(ns.getHint(b, h).equals(move1));
    p.removeFromHand(tile1);
    b.putTile(move1);
    assertTrue(ns.getHint(b, h).equals(move2));
    p.removeFromHand(tile2);
    b.putTile(move2);
    assertTrue(ns.getHint(b, h).equals(move3));
  }

}
