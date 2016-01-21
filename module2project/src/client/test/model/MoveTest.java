package client.test.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import client.model.Move;
import client.model.Tile;

public class MoveTest {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testEquals1() {
    Move move1 = new Move(new Tile("R", "o"), 91, 91);
    Move move2 = new Move(new Tile("R", "o"), 91, 91);
    Move move3 = new Move(new Tile("R", "b"), 91, 91);
    Move move4 = new Move(new Tile("R", "o"), 92, 91);
    assertTrue(move1.equals(move2));
    assertFalse(move1.equals(move3));
    assertFalse(move1.equals(move4));
  }
  
  @Test
  public void testEquals2() {
    Move move1 = new Move(new Tile("R", "o"));
    Move move2 = new Move(new Tile("R", "o"));
    Move move3 = new Move(new Tile("R", "b"));
    assertTrue(move1.equals(move2));
    assertFalse(move1.equals(move3));
  }
  
  @Test
  public void testEquals3() {
    Move move1 = new Move();
    Move move2 = new Move();
    assertTrue(move1.equals(move2));
  }
  
  @Test
  public void testEquals4() {
    Move move1 = new Move(new Tile("R", "o"), 91, 91);
    Move move2 = new Move(new Tile("R", "o"));
    Move move3 = new Move();
    assertFalse(move1.equals(move2));
    assertFalse(move1.equals(move3));
    assertFalse(move2.equals(move3));
  }
  
  @Test
  public void testToString() {
    Move move1 = new Move(new Tile("R", "o"), 91, 91);
    assertTrue(move1.toString().equals("[Ro, 91, 91]"));
  }
  
  @Test
  public void testColourToString() {
    Move move1 = new Move(new Tile("R", "o"), 91, 91);
    String string = "[" + Tile.ANSI_RED + "A" + Tile.ANSI_RESET + ", 91, 91]";
    assertTrue(move1.colourToString().equals(string));
  }

}
