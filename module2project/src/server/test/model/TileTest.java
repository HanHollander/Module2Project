package server.test.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import server.model.Tile;

public class TileTest {

  private Tile tile1;
  private Tile tile2;
  private Tile tile3;
  private Tile tile4;
  private Tile tile5;
  private Tile tile6;
  
  @Before
  public void setUp() throws Exception {
    tile1 = new Tile("R", "o");
    tile2 = new Tile("G", "d");
    tile3 = new Tile("B", "s");
    tile4 = new Tile("P", "c");
    tile5 = new Tile("Y", "x");
    tile6 = new Tile("O", "*");
  }

  @Test 
  public void testGetColor() {
    assertEquals(tile1.getColor(), "R");
    assertEquals(tile2.getColor(), "G");
    assertEquals(tile3.getColor(), "B");
    assertEquals(tile4.getColor(), "P");
    assertEquals(tile5.getColor(), "Y");
    assertEquals(tile6.getColor(), "O");
  }
  
  @Test 
  public void testGetShape() {
    assertEquals(tile1.getShape(), "o");
    assertEquals(tile2.getShape(), "d");
    assertEquals(tile3.getShape(), "s");
    assertEquals(tile4.getShape(), "c");
    assertEquals(tile5.getShape(), "x");
    assertEquals(tile6.getShape(), "*");
  }
  
  @Test
  public void testToString() {
    assertTrue(tile1.toString().equals(tile1.getColor() + tile1.getShape()));
    assertTrue(tile2.toString().equals(tile2.getColor() + tile2.getShape()));
    assertTrue(tile3.toString().equals(tile3.getColor() + tile3.getShape()));
    assertTrue(tile4.toString().equals(tile4.getColor() + tile4.getShape()));
    assertTrue(tile5.toString().equals(tile5.getColor() + tile5.getShape()));
    assertTrue(tile6.toString().equals(tile6.getColor() + tile6.getShape()));
  }

}
