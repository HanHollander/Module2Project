package server.test.controller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

import server.controller.ClientHandler;
import server.controller.Server;
import server.model.Player;
import server.model.Tile;

import org.junit.Test;

import exceptions.HandIsFullException;

public class ServerTest {

  private Server s;
  
  @Before
  public void setUp() throws Exception {
    s = new Server(null, 2, 999999, null, 1);
  }
  
  @Test
  public void testServerConstructor() {
    assertEquals(1, s.getServerNr());
    assertTrue(s.getObserver() instanceof server.view.Tuiview);
    assertEquals(0, s.getPlayerNrs().size());
    assertEquals(null, s.getThread(1));
    assertEquals(null, s.getThread(2));
    assertEquals(null, s.getThread(3));
    assertEquals(null, s.getThread(4));
    assertTrue(s.isReady());
    assertTrue(s.getGame() instanceof server.model.Game);
    assertTrue(s.allPlayerNamesAreKnown());
  }
  
  @Test
  public void testHandlerList() throws IOException {
    ClientHandler ch = null;
    try {
      ch = new ClientHandler(1, s);
    } catch (NullPointerException e) {
      // This happens because there is no socket to get a stream from
    }
    s.addHandler(1, ch);
    assertEquals(ch, s.getThread(1));
    assertTrue(s.getPlayerNrs().contains(1));
    assertFalse(s.allPlayerNamesAreKnown());
    s.removeHandler(1);
    assertFalse(s.getPlayerNrs().contains(1));
  }
  
  @Test
  public void testNextPlayerTurn() throws Exception {
    ClientHandler ch = null;
    s.addHandler(1, ch);
    s.getGame().addPlayer(1, "test1");
    assertTrue(s.allPlayerNamesAreKnown());
    s.addHandler(2, null);
    assertFalse(s.allPlayerNamesAreKnown());
    s.getGame().addPlayer(2, "test2");
    assertTrue(s.allPlayerNamesAreKnown());
    assertEquals(0, s.getGame().getCurrentPlayer());
    try {
      s.nextPlayerTurn();
    } catch (NullPointerException e) {
      // This happens because there are no clientHandlers to send stuff to
    }
    assertEquals(1, s.getGame().getCurrentPlayer());
    try {
      s.nextPlayerTurn();
    } catch (NullPointerException e) {
      // This happens because there are no clientHandlers to send stuff to
    }
    assertEquals(2, s.getGame().getCurrentPlayer());
    try {
      s.nextPlayerTurn();
    } catch (NullPointerException e) {
      // This happens because there are no clientHandlers to send stuff to
    }
    assertEquals(1, s.getGame().getCurrentPlayer());
    try {
      s.nextPlayerTurn();
    } catch (NullPointerException e) {
      // This happens because there are no clientHandlers to send stuff to
    }
    assertEquals(2, s.getGame().getCurrentPlayer());
  }
  
  @Test
  public void testKick() throws HandIsFullException {
    assertEquals(108, s.getGame().getPoolSize());
    s.addHandler(1, null);
    s.getGame().addPlayer(1, "test1");
    s.getGame().getPlayer(1).addToHand(s.getGame().drawRandomTileFromPool());
    s.getGame().getPlayer(1).addToHand(s.getGame().drawRandomTileFromPool());
    s.getGame().getPlayer(1).addToHand(s.getGame().drawRandomTileFromPool());
    assertEquals(105, s.getGame().getPoolSize());
    assertEquals(null, s.getThread(1));
    assertTrue(s.getPlayerNrs().contains(1));
    try {
      s.kick(1, "I did not like him");
    } catch (NullPointerException e) {
      // This happens because there are no clientHandlers to send stuff to
    }
    assertFalse(s.getGame().getPlayerNrs().contains(1));
    assertEquals(108, s.getGame().getPoolSize());
    s.addHandler(2, null);
    s.getGame().addPlayer(2, "test2");
    try {
      s.kick(1, "I did not like him");
    } catch (NullPointerException e) {
      // This happens because there are no clientHandlers to send stuff to
    }
    assertEquals(108, s.getGame().getPoolSize());
  }

}
