package client.test.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import client.model.Player;
import client.model.Board;
import client.model.ComputerPlayer;
import client.model.Move;
import client.model.NaiveStrategy;
import client.model.SmartStrategy;
import client.model.Tile;
import exceptions.InvalidMoveException;

public class ComputerPlayerTest {
  
  private Player p1;
  private Player p2;
  private Board b;

  @Before
  public void setUp() throws Exception {
    p1 = new ComputerPlayer("p1", 1, new NaiveStrategy());
    p2 = new ComputerPlayer("p2", 2, new SmartStrategy());
    p1.addToHand(new Tile("R", "o"));
    p1.addToHand(new Tile("B", "o"));
    p1.addToHand(new Tile("Y", "o"));
    p1.addToHand(new Tile("G", "o"));
    p2.addToHand(new Tile("R", "o"));
    p2.addToHand(new Tile("B", "o"));
    p2.addToHand(new Tile("Y", "o"));
    p2.addToHand(new Tile("G", "o"));
    b = new Board();
  }

  @Test
  public void testDetermineMove() throws InvalidMoveException {
    Move move1 = p1.determineMove(b);
    Move move2 = p2.determineMove(b);
    assertTrue(b.checkMove(move1));
    assertTrue(b.checkMove(move2));
    
    
  }

}
