package client.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import client.controller.Game;
import client.model.Board;
import client.model.HumanPlayer;
import client.model.Move;
import client.model.Tile;

public class GameTest {

  private Game g;
  
  @Before
  public void setUp() throws Exception {
    g = new Game("Game", InetAddress.getByName("localhost"), 7777, "h");
  }

  @Test
  public void testGame() throws UnknownHostException {
    Game g2 = new Game("Game", InetAddress.getByName("localhost"), 8888, "c");
  }
  
  @Test
  public void testPlayerTurn() {
    g.setPlayer(new HumanPlayer("Han", 0));
    ByteArrayInputStream in = new ByteArrayInputStream("END".getBytes());  
    System.setIn(in);
    g.playerTurn();
    System.setIn(System.in);
  }
  
  @Test
  public void testEndTurn1() {
    g.setPlayer(new HumanPlayer("Han", 0));
    g.getPlayer().getMoves().add(new Move(new Tile("R", "o"), 91, 91));
    g.endTurn();
  }
  
  @Test
  public void testEndTurn2() {
    g.setPlayer(new HumanPlayer("Han", 0));
    g.getPlayer().getMoves().add(new Move(new Tile("R", "o")));
    g.endTurn();
  }
  
  @Test
  public void testOpponentTurn() {
    List<Move> list = new ArrayList<>();
    list.add(new Move());
    //g.opponentTurn(list);
  }
  
  @Test
  public void testMakeMove() {
    g.setPlayer(new HumanPlayer("Han", 0));
    ByteArrayInputStream in = new ByteArrayInputStream("MOVE Ro 91 91".getBytes());  
    System.setIn(in);
    g.makeMove();    
    System.setIn(System.in);
    
  }

}
