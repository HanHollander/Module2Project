package client.test.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import client.model.Tile;

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
  public void testColourToString() {
    assertTrue(tile1.colourToString().equals(Tile.ANSI_RED + "A" + Tile.ANSI_RESET));
    assertTrue(tile2.colourToString().equals(Tile.ANSI_GREEN + "B" + Tile.ANSI_RESET));
    assertTrue(tile3.colourToString().equals(Tile.ANSI_WHITE + "C" + Tile.ANSI_RESET));
    assertTrue(tile4.colourToString().equals(Tile.ANSI_PURPLE + "D" + Tile.ANSI_RESET));
    assertTrue(tile5.colourToString().equals(Tile.ANSI_CYAN + "E" + Tile.ANSI_RESET));
    assertTrue(tile6.colourToString().equals(Tile.ANSI_YELLOW + "F" + Tile.ANSI_RESET));

  }

}
