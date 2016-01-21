package client.test.model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import client.model.Board;
import client.model.ComputerPlayer;
import client.model.Move;
import client.model.Move.Type;
import client.model.NaiveStrategy;
import client.model.SmartStrategy;
import client.model.Tile;
import exceptions.HandIsFullException;
import exceptions.TileNotInHandException;

public class SmartStrategyTest {

  private SmartStrategy ss;
  private Board b;
  private ComputerPlayer p;
  private List<Tile> h;
  
  @Before
  public void setUp() throws Exception {
    ss = new SmartStrategy();
    b = new Board();
    b.putTile(new Move(new Tile("R", "o"), 91, 91));
    b.endTurn();
    p = new ComputerPlayer("p", 1, ss);
  }

  @Test
  public void testDetermineMove() throws TileNotInHandException, HandIsFullException {
    p.addToHand(new Tile("R", "a"));
    p.addToHand(new Tile("B", "a"));
    p.addToHand(new Tile("Y", "o"));
    p.addToHand(new Tile("G", "o"));
    h = p.getHand();
    assertTrue(b.checkMove(ss.determineMove(b, h, p)));
  }
  
  @Test
  public void testGetBestSwapMove() throws HandIsFullException {
    p.addToHand(new Tile("B", "a"));
    h = p.getHand();
    assertTrue(ss.determineMove(b, h, p).getType().equals(Type.SWAP));
  }
}