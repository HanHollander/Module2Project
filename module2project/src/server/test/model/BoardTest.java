package server.test.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import server.model.*;

import org.junit.Before;
import org.junit.Test;

public class BoardTest {
  
  private Board b;

  @Before
  public void setUp() throws Exception {
    b = new Board();
    b.putTile(new Move(new Tile("R", "o"), 91, 91));
    b.putTile(new Move(new Tile("B", "o"), 90, 91));
    b.putTile(new Move(new Tile("G", "o"), 92, 91));
    b.endTurn();
  }

  @Test
  public void testCheckMove() {
    Move move1 = new Move(new Tile("P", "o"), 89, 91);
    Move move2 = new Move(new Tile("R", "x"), 91, 92);
    Move move3 = new Move(new Tile("P", "x"), 91, 92);
    assertTrue(b.checkMove(move1));
    assertTrue(b.checkMove(move2));
    assertFalse(b.checkMove(move3));
    Board b2 = new Board();
    b2.putTile(new Move(new Tile("R", "o"), 91, 90));
    b2.putTile(new Move(new Tile("B", "o"), 91, 91));
    b2.putTile(new Move(new Tile("G", "o"), 91, 92));
    b2.putTile(new Move(new Tile("P", "o"), 91, 94));
    b2.putTile(new Move(new Tile("Y", "o"), 91, 95));
    b2.putTile(new Move(new Tile("C", "o"), 91, 96));
    b2.endTurn();
    assertFalse(b2.checkMove(new Move(new Tile("R", "o"), 91, 93)));
    Board b3 = new Board();
    b3.putTile(new Move(new Tile("C", "o"), 91, 90));
    b3.putTile(new Move(new Tile("B", "o"), 91, 91));
    b3.putTile(new Move(new Tile("G", "o"), 91, 92));
    b3.putTile(new Move(new Tile("P", "o"), 91, 94));
    b3.putTile(new Move(new Tile("Y", "o"), 91, 95));
    b3.putTile(new Move(new Tile("C", "o"), 91, 96));
    b3.endTurn();
    assertFalse(b3.checkMove(new Move(new Tile("R", "o"), 91, 93)));
  }
  
  @Test
  public void testUndoMove() {
    Board before = b;
    Move move1 = new Move(new Tile("P", "o"), 89, 91);
    b.putTile(move1);
    b.undoMove(move1);
    assertTrue(b.equals(before));
  }
  
  @Test
  public void testResetTurn() {
    Board before = b;
    Move move1 = new Move(new Tile("P", "o"), 89, 91);
    Move move2 = new Move(new Tile("R", "x"), 91, 92);
    b.putTile(move1);
    b.putTile(move2);
    b.resetTurn();
    assertTrue(b.equals(before));
  }
  
  @Test
  public void testGetMargins1() {
    List<List<Integer>> margins = new ArrayList<>();
    List<Integer> rows = new ArrayList<>();
    rows.add(89);
    rows.add(93);
    List<Integer> columns = new ArrayList<>();
    columns.add(90);
    columns.add(92);
    margins.add(rows);
    margins.add(columns);
    assertTrue(b.getMargins().equals(margins));
  }
  
  @Test
  public void testGetMargins2() {
    Board b = new Board();
    List<List<Integer>> margins = new ArrayList<>();
    List<Integer> rows = new ArrayList<>();
    rows.add(90);
    rows.add(92);
    List<Integer> columns = new ArrayList<>();
    columns.add(90);
    columns.add(92);
    margins.add(rows);
    margins.add(columns);
    assertTrue(b.getMargins().equals(margins));
  }
  
  @Test
  public void testDeepCopy() {
    Board c = b.deepCopy();
    assertTrue(b.equals(c));
  }
  
  @Test
  public void testCurrentMovesLineUp() {
    Move move1 = new Move(new Tile("P", "o"), 89, 91);
    Move move2 = new Move(new Tile("Y", "o"), 88, 91);
    b.putTile(move1);
    assertTrue(b.currentMovesLineUp(move2));
  }
  
  @Test
  public void testGetScoreCurrentTurn() {
    Move move1 = new Move(new Tile("P", "o"), 89, 91);
    Move move2 = new Move(new Tile("Y", "o"), 88, 91);
    Move move3 = new Move(new Tile("O", "o"), 87, 91);
    b.putTile(move1);
    assertEquals(4, b.getScoreCurrentTurn());
    b.putTile(move2);
    b.putTile(move3);
    assertEquals(12, b.getScoreCurrentTurn());
    b.endTurn();
    Move move4 = new Move(new Tile("B", "o"), 87, 92);
    Move move5 = new Move(new Tile("R", "o"), 88, 92);
    Move move6 = new Move(new Tile("G", "o"), 89, 92);
    Move move7 = new Move(new Tile("P", "o"), 90, 92);
    Move move8 = new Move(new Tile("Y", "o"), 91, 92);
    Move move9 = new Move(new Tile("O", "o"), 92, 92);
    b.putTile(move4);
    b.putTile(move5);
    b.putTile(move6);
    b.putTile(move7);
    b.putTile(move8);
    b.putTile(move9);
    assertEquals(24, b.getScoreCurrentTurn());
  }
}
