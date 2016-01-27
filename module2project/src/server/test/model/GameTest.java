package server.test.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import exceptions.HandIsFullException;
import exceptions.TileNotInHandException;
import server.controller.ClientHandler;
import server.controller.Server;
import server.model.Tile;
import server.model.Move;

public class GameTest {

  private Server s;
  
  @Before
  public void setUp() {
    s = new Server(null, 2, 999999, null, 1);
    s.getGame().addPlayer(1, "Test1");
    s.getGame().addPlayer(2, "Test2");
  }
  
  @Test
  public void testGetWinningPlayerNr() {
    s.getGame().getPlayer(1).addToScore(3);
    assertEquals(1, s.getGame().getWinningPlayerNr());
  }

  @Test
  public void testGame() {
    try {
      s.getGame().getPlayer(1).addToHand(new Tile("P", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("G", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("Y", "x"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    try {
      s.getGame().getPlayer(2).addToHand(new Tile("R", "o"));
      s.getGame().getPlayer(2).addToHand(new Tile("G", "o"));
      s.getGame().getPlayer(2).addToHand(new Tile("B", "x"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    assertEquals(s, s.getGame().getServer());
    assertEquals(108, s.getGame().getPoolSize());
    assertFalse(null == s.getGame().getBoard());
    assertTrue(s.getGame().getBoard().getTile(91, 91).equals(new Tile()));
    assertEquals(0, s.getGame().getCurrentPlayer());
    s.getGame().setCurrentPlayer(1);
    assertEquals(1, s.getGame().getCurrentPlayer());
    assertEquals("Test1", s.getGame().getPlayer(1).getName());
    assertEquals("Test2", s.getGame().getPlayer(2).getName());
    Set<Integer> playerNrs = new HashSet<Integer>();
    playerNrs.add(1);
    playerNrs.add(2);
    assertEquals(playerNrs, s.getGame().getPlayerNrs());
    assertFalse(s.getGame().isGameOver());
    s.getGame().removePlayer(1);
    assertFalse(s.getGame().getPlayerNrs().contains(1));
  }
  
  @Test
  public void testPool() {
    assertEquals(108, s.getGame().getPoolSize());
    try {
      s.getGame().dealTiles();
    } catch (NullPointerException e) {
      // This happens because there are no clientHandlers to send stuff to
    }
    assertEquals(96, s.getGame().getPoolSize());
    assertEquals(6, s.getGame().getPlayer(1).getHand().size());
    assertEquals(6, s.getGame().getPlayer(2).getHand().size());
    int size = s.getGame().getPoolSize();
    assertTrue(s.getGame().swapTileWithPool(new Tile("R", "o")) instanceof Tile);
    assertEquals(size, s.getGame().getPoolSize());
    s.getGame().addTileToPool(new Tile("R", "o"));
    assertEquals(size + 1, s.getGame().getPoolSize());
    assertTrue(s.getGame().drawRandomTileFromPool() instanceof Tile);
    assertEquals(size, s.getGame().getPoolSize());
  }
  
  @Test
  public void testIsGameOver() {
    assertEquals(108, s.getGame().getPoolSize());
    try {
      s.getGame().getPlayer(1).addToHand(new Tile("P", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("G", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("Y", "x"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    try {
      s.getGame().getPlayer(2).addToHand(new Tile("R", "o"));
      s.getGame().getPlayer(2).addToHand(new Tile("G", "o"));
      s.getGame().getPlayer(2).addToHand(new Tile("B", "x"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    assertFalse(s.getGame().isGameOver());
    while (s.getGame().getPoolSize() > 0) {
      s.getGame().drawRandomTileFromPool();
    }
    assertFalse(s.getGame().isGameOver());
    for (Tile tile : s.getGame().getPlayer(1).getHand()) {
      try {
        s.getGame().getPlayer(1).removeFromHand(tile);
      } catch (TileNotInHandException e) {
        System.out.println(e);
      }
    }
    assertTrue(s.getGame().isGameOver());
  }
  
  @Test
  public void testApplyMoveTurn() {
    try {
      s.getGame().getPlayer(1).addToHand(new Tile("P", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("G", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("Y", "x"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    try {
      s.getGame().getPlayer(2).addToHand(new Tile("R", "o"));
      s.getGame().getPlayer(2).addToHand(new Tile("G", "o"));
      s.getGame().getPlayer(2).addToHand(new Tile("B", "x"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    assertEquals(108, s.getGame().getPoolSize());
    List<Move> turn1 = new ArrayList<Move>();
    turn1.add(new Move(new Tile("P", "o"), 91, 91));
    turn1.add(new Move(new Tile("G", "o"), 91, 92));
    s.getGame().applyMoveTurn(s.getGame().getPlayer(1), turn1, true);
    for (Move move : turn1) {
      assertTrue(s.getGame().getBoard().getTile(move.getRow(), move.getColumn()).equals(move.getTile()));
    }
    assertEquals(108 - turn1.size(), s.getGame().getPoolSize());
    assertEquals(3, s.getGame().getPlayer(1).getHand().size());
  }
  
  @Test
  public void testApplySwapTurn() {
    try {
      s.getGame().getPlayer(1).addToHand(new Tile("P", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("G", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("Y", "x"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    try {
      s.getGame().getPlayer(2).addToHand(new Tile("R", "o"));
      s.getGame().getPlayer(2).addToHand(new Tile("G", "o"));
      s.getGame().getPlayer(2).addToHand(new Tile("B", "x"));
      s.getGame().getPlayer(2).addToHand(new Tile("B", "x"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    assertEquals(108, s.getGame().getPoolSize());
    List<Tile> turn1 = new ArrayList<Tile>();
    turn1.add(new Tile("P", "o"));
    turn1.add(new Tile("G", "o"));
    try {
      s.getGame().applySwapTurn(turn1, s.getGame().getPlayer(1));
    } catch (NullPointerException e) {
      // This happens because there are no clientHandlers to send stuff to
    }
    assertEquals(108, s.getGame().getPoolSize());
    assertEquals(3, s.getGame().getPlayer(1).getHand().size());
    assertEquals(4, s.getGame().getPlayer(2).getHand().size());
  }
  
  @Test
  public void testCheckMoveTurn() {
    try {
      s.getGame().getPlayer(1).addToHand(new Tile("P", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("G", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("Y", "x"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    try {
      s.getGame().getPlayer(2).addToHand(new Tile("R", "o"));
      s.getGame().getPlayer(2).addToHand(new Tile("G", "o"));
      s.getGame().getPlayer(2).addToHand(new Tile("B", "x"));
      s.getGame().getPlayer(2).addToHand(new Tile("B", "x"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    assertEquals(108, s.getGame().getPoolSize());
    List<Move> turn1 = new ArrayList<Move>();
    turn1.add(new Move(new Tile("P", "o"), 91, 91));
    turn1.add(new Move(new Tile("G", "o"), 91, 92));
    assertTrue(s.getGame().checkMoveTurn(turn1, s.getGame().getPlayer(1)));
    for (Move move : turn1) {
      assertFalse(s.getGame().getBoard().getTile(move.getRow(), move.getColumn()).equals(move.getTile()));
    }
    assertEquals(108, s.getGame().getPoolSize());
    assertEquals(3, s.getGame().getPlayer(1).getHand().size());
    assertEquals(4, s.getGame().getPlayer(2).getHand().size());
    List<Move> turn2 = new ArrayList<Move>();
    turn2.add(new Move(new Tile("P", "x"), 91, 91));
    turn2.add(new Move(new Tile("G", "x"), 91, 92));
    assertFalse(s.getGame().checkMoveTurn(turn2, s.getGame().getPlayer(1)));
    for (Move move : turn2) {
      assertFalse(s.getGame().getBoard().getTile(move.getRow(), move.getColumn()).equals(move.getTile()));
    }
    assertEquals(108, s.getGame().getPoolSize());
    assertEquals(3, s.getGame().getPlayer(1).getHand().size());
    assertEquals(4, s.getGame().getPlayer(2).getHand().size());
    List<Move> turn3 = new ArrayList<Move>();
    turn3.add(new Move(new Tile("P", "o"), 93, 91));
    turn3.add(new Move(new Tile("G", "o"), 91, 92));
    assertFalse(s.getGame().checkMoveTurn(turn3, s.getGame().getPlayer(1)));
    for (Move move : turn3) {
      assertFalse(s.getGame().getBoard().getTile(move.getRow(), move.getColumn()).equals(move.getTile()));
    }
    assertEquals(108, s.getGame().getPoolSize());
    assertEquals(3, s.getGame().getPlayer(1).getHand().size());
    assertEquals(4, s.getGame().getPlayer(2).getHand().size());
  }
  
  @Test
  public void testCheckSwapTurn() {
    try {
      s.getGame().getPlayer(1).addToHand(new Tile("P", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("G", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("Y", "x"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    try {
      s.getGame().getPlayer(2).addToHand(new Tile("R", "o"));
      s.getGame().getPlayer(2).addToHand(new Tile("G", "o"));
      s.getGame().getPlayer(2).addToHand(new Tile("B", "x"));
      s.getGame().getPlayer(2).addToHand(new Tile("B", "x"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    assertEquals(108, s.getGame().getPoolSize());
    List<Tile> turn1 = new ArrayList<Tile>();
    turn1.add(new Tile("P", "o"));
    turn1.add(new Tile("G", "o"));
    assertTrue(s.getGame().checkSwapTurn(turn1, s.getGame().getPlayer(1)));
    assertEquals(108, s.getGame().getPoolSize());
    assertEquals(3, s.getGame().getPlayer(1).getHand().size());
    assertEquals(4, s.getGame().getPlayer(2).getHand().size());
    List<Tile> turn2 = new ArrayList<Tile>();
    turn2.add(new Tile("P", "x"));
    turn2.add(new Tile("G", "x"));
    assertFalse(s.getGame().checkSwapTurn(turn2, s.getGame().getPlayer(1)));
    assertEquals(108, s.getGame().getPoolSize());
    assertEquals(3, s.getGame().getPlayer(1).getHand().size());
    assertEquals(4, s.getGame().getPlayer(2).getHand().size());
    while (s.getGame().getPoolSize() > 0) {
      s.getGame().drawRandomTileFromPool();
    }
    assertFalse(s.getGame().checkSwapTurn(turn1, s.getGame().getPlayer(1)));
  }
  
  @Test
  public void testGetPossiblePlaces() {
    List<List<Integer>> places = new ArrayList<List<Integer>>();
    places.add(new ArrayList<Integer>());
    places.get(0).add(91);
    places.get(0).add(91);
    assertEquals(places, s.getGame().getPossiblePlaces(s.getGame().getBoard()));
    s.getGame().getBoard().putTile(new Move(new Tile("R", "o"), 91, 91));
    List<List<Integer>> places2 = new ArrayList<List<Integer>>();
    places2.add(new ArrayList<Integer>());
    places2.get(0).add(91);
    places2.get(0).add(90);
    places2.add(new ArrayList<Integer>());
    places2.get(1).add(91);
    places2.get(1).add(92);
    places2.add(new ArrayList<Integer>());
    places2.get(2).add(90);
    places2.get(2).add(91);
    places2.add(new ArrayList<Integer>());
    places2.get(3).add(92);
    places2.get(3).add(91);
    assertTrue(s.getGame().getPossiblePlaces(s.getGame().getBoard()).containsAll(places2));
  }
  
  @Test
  public void testMovePossible() {
    try {
      s.getGame().getPlayer(1).addToHand(new Tile("P", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("G", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("Y", "x"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    try {
      s.getGame().getPlayer(2).addToHand(new Tile("R", "o"));
      s.getGame().getPlayer(2).addToHand(new Tile("G", "o"));
      s.getGame().getPlayer(2).addToHand(new Tile("B", "x"));
      s.getGame().getPlayer(2).addToHand(new Tile("B", "*"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    assertTrue(s.getGame().movePossible(1));
    assertTrue(s.getGame().movePossible(2));
    while (s.getGame().getPoolSize() > 0) {
      s.getGame().drawRandomTileFromPool();
    }
    assertTrue(s.getGame().movePossible(1));
    assertTrue(s.getGame().movePossible(2));
    s.getGame().getBoard().putTile(new Move(new Tile("O", "*"), 91, 91));
    assertFalse(s.getGame().movePossible(1));
    assertTrue(s.getGame().movePossible(2));
  }
  
  @Test
  public void testCalculatePlayerNrWithBestHand() {
    try {
      s.getGame().getPlayer(1).addToHand(new Tile("P", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("G", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("Y", "x"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    try {
      s.getGame().getPlayer(2).addToHand(new Tile("R", "o"));
      s.getGame().getPlayer(2).addToHand(new Tile("G", "o"));
      s.getGame().getPlayer(2).addToHand(new Tile("B", "x"));
      s.getGame().getPlayer(2).addToHand(new Tile("B", "*"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    assertEquals(1, s.getGame().calculatePlayerNrWithBestHand());
    try {
      s.getGame().getPlayer(2).addToHand(new Tile("Y", "o"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    assertEquals(2, s.getGame().calculatePlayerNrWithBestHand());
    try {
      s.getGame().getPlayer(1).addToHand(new Tile("B", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("R", "o"));
      s.getGame().getPlayer(1).addToHand(new Tile("Y", "*"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    assertEquals(1, s.getGame().calculatePlayerNrWithBestHand());
    try {
      s.getGame().getPlayer(2).addToHand(new Tile("C", "o"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
  }
}
