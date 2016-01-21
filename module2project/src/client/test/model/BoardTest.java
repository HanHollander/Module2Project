package client.test.model;

import static org.junit.Assert.*;
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

}
