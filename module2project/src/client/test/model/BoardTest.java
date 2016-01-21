package client.test.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import client.model.*;

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
    System.out.println(b.getMargins());
    System.out.println(margins);
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
    System.out.println(b.getMargins());
    System.out.println(margins);
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
    b.putTile(move1);
    assertTrue(b.getScoreCurrentTurn() == 4);
  }
  
  
  
  
  
  
  
  
  
  
  
  

}
